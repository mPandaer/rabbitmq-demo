package com.pandaer.examples.logger;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.ExchangeNameConstant.LOGGER_EXCHANGE;

public class LogReceiver {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();

        // 确保交换机存在
        channel.exchangeDeclare(LOGGER_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 将队列与交换机绑定
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,LOGGER_EXCHANGE,"");

        channel.basicConsume(queueName,(consumerTag, message) -> {
            System.out.println("接收到日志：" + new String(message.getBody(), StandardCharsets.UTF_8));
        },consumerTag -> {});

    }
}
