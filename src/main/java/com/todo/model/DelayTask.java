package com.todo.model;

import com.todo.domain.Task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayTask implements Delayed {
    private Task task;
    private long expire;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof DelayTask){
            return this.getTask().getId() == ((DelayTask)obj).getTask().getId();
        }else {
            return false;
        }
    }
}
