package com.pandaer.examples.loggerv3;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.ExchangeNameConstant.LOGGER_V3;

public abstract class LogReceiver {

    abstract List<String> getBindingKeys();

    /**
     * 接收日志
     */
    public void receiveLog() throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        // 确保交换机存在
        channel.exchangeDeclare(LOGGER_V3, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();
        List<String> bindingKeys = getBindingKeys();
        for (String bindingKey : bindingKeys) {
            channel.queueBind(queueName,LOGGER_V3,bindingKey);
        }

        // 接收消息
        channel.basicConsume(queueName,(consumerTag, message) -> {
            System.out.println("接收到日志：" + new String(message.getBody(), StandardCharsets.UTF_8));
        },consumerTag -> {});
    }
}
