package com.todo.bean;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "testQueue")
public class ExpireListener {
    @RabbitHandler
    public void handler(String message) throws IOException {
        System.out.println("Receive a message:"+message);
    }
}
