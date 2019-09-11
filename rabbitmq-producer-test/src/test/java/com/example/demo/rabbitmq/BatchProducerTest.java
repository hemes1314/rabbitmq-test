package com.example.demo.rabbitmq;

import com.example.demo.DemoApplication;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@ActiveProfiles("node0")
@SpringBootTest(classes = DemoApplication.class)
public class BatchProducerTest {

    @Value("${profile_name}")
    private String profileName;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    private static final String username = "admin";
    private static final String password = "admin";
    private static final String virtualHost = "/datacanvas";
    private static final String host = "192.168.4.17";
    private static final Integer port = 31888;

    @Test
    public void createQueue() throws IOException, TimeoutException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+profileName);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(host);
        factory.setPort(port);

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        // durable 服务器重启会保留下来Exchange。警告：仅设置此选项，不代表消息持久化。即不保证重启后消息还在。
        // exclusive 是否排外的，有两个作用，一：当连接关闭时connection.close()该队列是否会自动删除；二：该队列是否是私有的private，如果不是排外的，可以使用两个消费者都访问同一个队列，没有任何问题，如果是排外的，会对当前队列加锁，其他通道channel是不能访问的，如果强制访问会报异常：com.rabbitmq.client.ShutdownSignalException: channel error; protocol method: #method<channel.close>(reply-code=405, reply-text=RESOURCE_LOCKED - cannot obtain exclusive access to locked queue 'queue_name' in vhost '/', class-id=50, method-id=20)一般等于true的话用于一个队列只能有一个消费者来消费的场景
        // autoDelete 是否自动删除，当最后一个消费者断开连接之后队列是否自动被删除，可以通过RabbitMQ Management，查看某个队列的消费者数量，当consumers = 0时队列就会自动删除
        // arguments：队列中的消息什么时候会自动被删除
        String prefix = "q_test_wubb_0910_";
        for(int i = 1000; i < 2000; i++) {
//            String queueName = channel.queueDeclare("", true, false, false, null).getQueue(); // 匿名q
            String queueName = channel.queueDeclare(prefix+i, true, false, false, null).getQueue();
            System.out.println(queueName);
            String message = profileName+"->msg_test_msg_"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN , message.getBytes());
            System.out.println(profileName+" publish msg:"+message);
        }
        channel.close();
        conn.close();
    }

    public static volatile int count = 0;

    @Test
    public void sendMsg() throws IOException, TimeoutException, InterruptedException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+profileName);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(host);
        factory.setPort(port);

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        String prefix = "q_test_wubb_0910_";

        while(true) {
            for(int i = 0; i < 2000; i++) {
                new Runnable(){
                    @Override
                    public void run() {
                        if(count+1 == 2000) {
                            count = 0;
                        }
                        String queueName = prefix + BatchProducerTest.count++;
                        String message = profileName+"->msg_test_msg_"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                        try {
                            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN , message.getBytes());
                        } catch (Exception e) {}
                        System.out.println(profileName+"-"+queueName+" publish msg:"+message);
                    }
                }.run();
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void deleteAll() throws IOException, TimeoutException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+profileName);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(host);
        factory.setPort(port);

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        String prefix = "q_test_wubb_0910_";
        for(int i = 0; i < 2000; i++) {
            channel.queueDelete(prefix+i);
            System.out.println("q_test_wubb_0910_"+i+" deleted.");
        }
        channel.close();
        conn.close();
    }
}
