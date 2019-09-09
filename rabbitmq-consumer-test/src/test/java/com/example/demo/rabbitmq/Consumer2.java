package com.example.demo.rabbitmq;

import com.example.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("node2")
@SpringBootTest(classes = DemoApplication.class)
public class Consumer2 {

    @Test
    public void test() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
        }
    }
}
