package com.example.demo.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    public static final String queue_test_01 = "q_test_wubb_0909";

    @Bean
    public Queue queue() {
        return new Queue(queue_test_01);
    }
}
