package com.example.demo.rabbitmq;

import com.example.demo.DemoApplication;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("node0")
@SpringBootTest(classes = DemoApplication.class)
@RabbitListener(queues = "q_test_wubb_0909")
public class Consumer0 {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("Node0 receive msg : " + msg);
    }
}
