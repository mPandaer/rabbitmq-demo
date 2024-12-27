package com.pandaer.examples.logger;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.ExchangeNameConstant.LOGGER_EXCHANGE;

/**
 * 产生日志的地方
 */
public class LogProvider {
    public static void main(String[] args) throws IOException, TimeoutException {
        int loggerCount = 80;
        int maxTime = 70000;
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();

        // 定义一个交换机
        channel.exchangeDeclare(LOGGER_EXCHANGE, BuiltinExchangeType.FANOUT);

        for (int i = 0; i<loggerCount;i++) {
            // 发送日志
            String content = String.format("第%d条日志：%d",i+1,i+1);
            channel.basicPublish(LOGGER_EXCHANGE,"",null,content.getBytes(StandardCharsets.UTF_8));
            int time = (int) (Math.random() * maxTime) + 1;
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
