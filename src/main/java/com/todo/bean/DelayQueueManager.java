package com.todo.bean;

import com.todo.domain.Task;
import com.todo.model.DelayTask;
import com.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
@Scope("singleton")
@Component
public class DelayQueueManager implements CommandLineRunner {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    TaskService taskService;
    @Value(value = "${myproperties.redis.message}")
    private String redisMessage;
    private DelayQueue<DelayTask> delayQueue = new DelayQueue<>();
    @Override
    public void run(String... args) throws Exception {
        Executors.newSingleThreadExecutor().execute(new Thread(this::excuteThread));
    }
    public void put(DelayTask delayTask){
        delayQueue.put(delayTask);
    }
    private void excuteThread() {
        while (true) {
            try {
                DelayTask task = delayQueue.take();
                processTask(task);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    private void processTask(DelayTask task) {
        String message = "您的任务'"+task.getTask().getTitle()+"'已经过期";
        /*redisTemplate.opsForList().rightPush(redisMessage,message);*/
        Task taskEnetity = task.getTask();
        taskEnetity.setStatus("已过期");
        taskService.updateStatusById(taskEnetity);
        simpMessagingTemplate.convertAndSend("/test/",message);
    }
}
