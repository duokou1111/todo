package com.todo.bean;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQBean {
    @Autowired
    @Lazy
    private DirectExchange basicExchange;
    @Autowired
    @Lazy
    private Queue basicQueue;
    @Bean
    public DirectExchange basicExchange(){
        return new DirectExchange("testExchange",true,false);
    }
    @Bean
    public Queue basicQueue(){
        return new Queue("testQueue",true);
    }
    @Bean
    Binding basicBinding(){
        return BindingBuilder.bind(basicQueue).to(basicExchange).with("testKey");
    }
}
