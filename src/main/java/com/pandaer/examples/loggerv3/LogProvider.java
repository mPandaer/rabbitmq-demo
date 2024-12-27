package com.pandaer.examples.loggerv3;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.ExchangeNameConstant.LOGGER_V3;

/**
 * 日志发送器
 */
public abstract class LogProvider {


    /**
     * 日志来源
     * @return
     */
    abstract String getLogSource();

    /**
     * 日志等级
     * @return
     */
    String getLogLevel() {
        String[] levels = {"error","warning","info"};
        int index = (int) (Math.random() * 3);
        return levels[index];
    }


    /**
     * 发送日志
     */
    public void sendLog(int logCount,int maxWaitTime) throws IOException, TimeoutException, InterruptedException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();

        // 创建交换机
        channel.exchangeDeclare(LOGGER_V3, BuiltinExchangeType.TOPIC);

        for (int i = 0; i < logCount;i++) {
            String logLevel = getLogLevel();
            String logSource = getLogSource();
            String content = String.format("[%s]-[%s] 第%d条日志", logLevel, logSource,i + 1);
            String routingKey = String.format("%s.%s", logLevel, logSource);
            channel.basicPublish(LOGGER_V3,routingKey,null,content.getBytes(StandardCharsets.UTF_8));
            int waitTime = (int) (Math.random() * maxWaitTime);
            Thread.sleep(waitTime);
        }
    }
}
