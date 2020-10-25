package com.todo.controller;

import com.todo.bean.DelayQueueManager;
import com.todo.domain.Task;
import com.todo.model.ActionResult;
import com.todo.model.DelayTask;
import com.todo.model.TaskDTO;
import com.todo.service.TaskService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private DelayQueueManager delayQueueManager;
   /* @GetMapping("/test")
    @ResponseBody
    public void test(){
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("20000");
        byte[] msgBytes = "测试消息过期".getBytes();
        Message message = new Message(msgBytes, messageProperties);
        rabbitTemplate.convertAndSend("testExchange","testKey","TestMessage");
    }*/

    @PostMapping("/task")
    @ResponseBody
    public ActionResult addTask(@Valid @RequestBody TaskDTO taskDTO, BindingResult bindingResult, HttpServletRequest request){

        ActionResult actionResult = new ActionResult();
        if(bindingResult.hasErrors()){
            actionResult.setMessage(bindingResult.getFieldError().getDefaultMessage());
            actionResult.setSuccess(false);
            return actionResult;
        }
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setExpireTime(taskDTO.getDate());
        task.setContent(taskDTO.getContent());
        task.setPriority(taskDTO.getPriority());
        Integer id = taskService.save(task);
        DelayTask delayTask = new DelayTask();
        delayTask.setTask(task);
        delayTask.setExpire(task.getExpireTime().getTime() - 28800000);
        delayQueueManager.put(delayTask);
        actionResult.setSuccess(true);
        return actionResult;
    }
    @GetMapping("/")
    public String index(Model model){
        List<Task> list = taskService.queryAll();
        for (Task task:list){
            System.out.println(task.toString());
        }
        model.addAttribute("list",list);
        return "index";
    }
    @GetMapping({"/date/{startTime}/{endTime}/sort/{sort}","/priority/{p1}/{p2}/{p3}/date/{startTime}/{endTime}/sort/{sort}","/priority/{p1}/{p2}/date/{startTime}/{endTime}/sort/{sort}","/priority/{p1}/date/{startTime}/{endTime}/sort/{sort}"})
    public String query(Model model,@PathVariable String sort,@PathVariable(required = false) String startTime,@PathVariable(required = false)  String endTime,@PathVariable(required = false)  Integer p1,@PathVariable(required = false)  Integer p2,@PathVariable(required = false)  Integer p3,HttpServletRequest httpServletRequest){
        System.out.println("url="+httpServletRequest.getRequestURL().toString());
        Date startDate = null;
        Date endDate = null;
        if(!StringUtils.isEmptyOrWhitespace(startTime) && !StringUtils.isEmptyOrWhitespace(endTime)){
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                startDate = simpleDateFormat.parse(startTime);
                endDate = simpleDateFormat.parse(endTime);
                if (startDate.compareTo(endDate) >= 0){
                    Date tmp = startDate;
                    startDate = endDate;
                    endDate = tmp;
                }
            }catch (Exception e){
                return "redirect:/";
            }
        }
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setP1(p1);
        taskDTO.setP2(p2);
        taskDTO.setP3(p3);
        taskDTO.setSort(sort);
        taskDTO.setStartTime(startDate);
        taskDTO.setEndTime(endDate);
        List<Task> list = taskService.query(taskDTO);
        for (Task task:list){
            System.out.println(task.toString());
        }
        model.addAttribute("list",list);
        return "index";
    }

    @PutMapping("/task")
    @ResponseBody
    public ActionResult updateTask(@RequestBody Task task,HttpServletRequest httpServletRequest){
        Integer num = taskService.updateStatusById(task);
        ActionResult actionResult = new ActionResult();
        if(num<1){
            actionResult.setSuccess(false);
            actionResult.setMessage("更新失败");
        }else {
            actionResult.setSuccess(true);
        }
        return actionResult;
    }
    @DeleteMapping("/task")
    @ResponseBody
    public ActionResult deleteTask(@RequestBody Task task,HttpServletRequest httpServletRequest){
        Integer num = taskService.deleteById(task);
        ActionResult actionResult = new ActionResult();
        if(num<1){
            actionResult.setSuccess(false);
            actionResult.setMessage("删除失败");
        }else {
            actionResult.setSuccess(true);
        }
        return actionResult;
    }
}
