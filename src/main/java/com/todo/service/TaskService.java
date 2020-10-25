package com.todo.service;

import com.todo.domain.Task;
import com.todo.model.TaskDTO;
import com.todo.respository.mapper.TaskMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskService {
    @Resource
    private TaskMapper taskMapper;
    public Integer save(Task task){
        return taskMapper.saveTask(task);
    }
    public List<Task> queryAll(){
        return taskMapper.queryAll();
    }
    public List<Task> query(TaskDTO taskDTO){return taskMapper.query(taskDTO);}
    public Integer updateStatusById(Task task){return taskMapper.updateStatusById(task);}
    public Integer deleteById(Task task){return taskMapper.deleteById(task);}
}
