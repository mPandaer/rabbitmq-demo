package com.pandaer.examples.helloworld;

import com.pandaer.utils.InputUtil;
import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.QueueNameConstant.HELLO_WORLD;

public class Sender {


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        while (true) {
            // 获取用户输入
            String message = InputUtil.getUserInput("请输入要发送的消息(exit表示退出)：");
            if ("exit".equals(message)) {
                channel.close();
                connect.close();
                break;
            }
            sendMessage(channel,HELLO_WORLD,message);
        }

    }

    private static void sendMessage(Channel channel,String queueName,String message) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes());
    }

}
