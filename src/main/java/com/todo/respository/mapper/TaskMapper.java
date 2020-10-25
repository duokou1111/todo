package com.todo.respository.mapper;

import com.todo.domain.Task;
import com.todo.model.TaskDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface TaskMapper {
    Integer saveTask(Task task);
    List<Task> queryAll();
    List<Task> query(TaskDTO taskDTO);
    Integer updateStatusById(Task task);
    Integer deleteById(Task task);
}
