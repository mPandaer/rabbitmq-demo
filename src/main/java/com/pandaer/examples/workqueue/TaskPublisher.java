package com.pandaer.examples.workqueue;

import com.pandaer.utils.InputUtil;
import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.QueueNameConstant.WORK_QUEUE;

/**
 * 任务发布者
 */
public class TaskPublisher {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        // 定义一个任务队列
        channel.queueDeclare(WORK_QUEUE,true,false,false,null);
        while (true) {
            // 获取任务名
            String taskName = InputUtil.getUserInput("请输入任务名(PS:exit表示退出)");
            if ("exit".equals(taskName)) {
                channel.close();
                connect.close();
                break;
            }

            System.out.println("发送任务：" + taskName);
            // 发送任务名
            channel.basicPublish("",WORK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN,taskName.getBytes(StandardCharsets.UTF_8));
        }
    }
}
