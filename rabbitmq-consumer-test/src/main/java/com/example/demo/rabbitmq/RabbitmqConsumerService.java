package com.example.demo.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "q_test_wubb_0909")
public class RabbitmqConsumerService {

    @Value("${profile_name}")
    private String profileName;

    @RabbitHandler
    public void process(String msg) throws InterruptedException {
        System.out.println(profileName+" receive msg : " + msg);
    }
}
