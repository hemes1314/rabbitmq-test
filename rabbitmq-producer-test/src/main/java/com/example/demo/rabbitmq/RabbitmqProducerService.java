package com.example.demo.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class RabbitmqProducerService {

    @Value("${profile_name}")
    private String profileName;

    @Resource
    private RabbitTemplate rabbitTemplate;

    RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean isAck, String cause) {
            System.out.println("=======ConfirmCallback=========");
            System.out.println("correlationData = " + correlationData);
            System.out.println("ack = " + isAck);
            System.out.println("cause = " + cause);
            System.out.println("=======ConfirmCallback=========");
        }
    };

    RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("--------------ReturnCallback----------------");
            System.out.println("message = " + message);
            System.out.println("replyCode = " + replyCode);
            System.out.println("replyText = " + replyText);
            System.out.println("exchange = " + exchange);
            System.out.println("routingKey = " + routingKey);
            System.out.println("--------------ReturnCallback----------------");
        }
    };

    public void sendMsg(String msg) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend(RabbitMQConfig.queue_test_01, msg);
        System.out.println(profileName+" publish msg : "+ msg);
    }

}
