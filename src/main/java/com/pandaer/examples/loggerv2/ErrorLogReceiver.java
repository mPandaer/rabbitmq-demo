package com.pandaer.examples.loggerv2;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.ExchangeNameConstant.LOGGER_V2;

public class ErrorLogReceiver {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        // 保证交换机存在
        channel.exchangeDeclare(LOGGER_V2, BuiltinExchangeType.DIRECT);

        // 创建队列
        String queueName = channel.queueDeclare().getQueue();

        // 绑定交换机
        channel.queueBind(queueName, LOGGER_V2,"error");

        // 接收消息
        channel.basicConsume(queueName,(consumerTag, message) -> {
            System.out.println("Error日志器，收到日志：" + new String(message.getBody(), StandardCharsets.UTF_8));
        },consumerTag -> {});

    }
}
