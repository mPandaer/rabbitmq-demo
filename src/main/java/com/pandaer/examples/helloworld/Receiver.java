package com.pandaer.examples.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.QueueNameConstant.HELLO_WORLD;

public class Receiver {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(HELLO_WORLD,false,false,false,null);
        channel.basicConsume(HELLO_WORLD, (tag,message) -> {
            String data = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息MQ: " + data);
        }, (tag) -> {});
        System.out.println("消费者启动成功");
    }
}
