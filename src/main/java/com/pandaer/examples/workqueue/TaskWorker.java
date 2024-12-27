package com.pandaer.examples.workqueue;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.QueueNameConstant.WORK_QUEUE;

public class TaskWorker {


    static CancelCallback cancelCallback = (String consumerTag) -> {
        System.out.println("cancelCallback");
    };

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        channel.queueDeclare(WORK_QUEUE,true,false,false,null);
        // 处理任务
        channel.basicQos(1);
        channel.basicConsume(WORK_QUEUE, (String consumerTag, Delivery message) -> {
            String taskName = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("开始执行任务：" + taskName);
            doWork(taskName);
            System.out.println("执行任务完成：" + taskName);
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        },cancelCallback);
    }

    private static void doWork(String taskName) {
        char[] charArray = taskName.toCharArray();
        for (char ch : charArray) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
