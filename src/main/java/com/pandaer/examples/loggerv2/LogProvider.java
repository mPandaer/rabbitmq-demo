package com.pandaer.examples.loggerv2;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.ExchangeNameConstant.LOGGER_V2;

public class LogProvider {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        List<String> logLevels = Arrays.asList("info","warning","error");
        int logCount = 50;
        int maxWaitTime = 30000;

        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        // 定义交换机
        channel.exchangeDeclare(LOGGER_V2, BuiltinExchangeType.DIRECT);

        for (int i = 0; i<logCount;i++) {
            // 随机选择一个日志等级
            int levelIndex = (int) (Math.random() * 3);
            String logLevel = logLevels.get(levelIndex);

            String content = String.format("logger pro: [%s] 第%d条日志",logLevel,i+1);
            channel.basicPublish(LOGGER_V2,logLevel,null,content.getBytes(StandardCharsets.UTF_8));

            // 随机等待时间
            int waitTime = (int) (Math.random() * maxWaitTime) + 1;
            Thread.sleep(waitTime);
        }

    }
}
