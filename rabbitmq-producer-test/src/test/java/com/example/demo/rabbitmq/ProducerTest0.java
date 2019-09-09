package com.example.demo.rabbitmq;

import com.example.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("node0")
@SpringBootTest(classes = DemoApplication.class)
public class ProducerTest0 {

    @Value("${profile_name}")
    private String profileName;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    @Test
    public void test() throws InterruptedException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+profileName);
        while (true) {
            rabbitmqProducerService.sendMsg("node0->msg_test_msg_"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            Thread.sleep(3000);
        }
    }

}
