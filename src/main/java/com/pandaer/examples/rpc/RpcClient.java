package com.pandaer.examples.rpc;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.QueueNameConstant.RPC_QUEUE;

public class RpcClient {


    /**
     * 函数调用
     * @param num
     * @return
     */
    public int call(int num) throws IOException, TimeoutException, ExecutionException, InterruptedException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();
        channel.queueDeclare(RPC_QUEUE,false,false,false,null);
        String callbackQueueName = channel.queueDeclare().getQueue();

        // 发送消息
        String uid = UUID.randomUUID().toString();
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .replyTo(callbackQueueName)
                .correlationId(uid)
                .build();

        // 发送请求
        channel.basicPublish("",RPC_QUEUE,basicProperties,String.valueOf(num).getBytes(StandardCharsets.UTF_8));

        CompletableFuture<Integer> future = new CompletableFuture<>();

        // 接收请求
        channel.basicConsume(callbackQueueName,(consumerTag, message) -> {
            String id = message.getProperties().getCorrelationId();
            if (!uid.equals(id)) {
                System.out.println("唯一ID不一致，不做任何处理");
                future.complete(-1);
                return;
            }
            future.complete(Integer.parseInt(new String(message.getBody(),StandardCharsets.UTF_8)));
        },consumerTag -> {});


        return future.get();

    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        RpcClient rpcClient = new RpcClient();
        for (int i = 0; i< 15; i++) {
            int res = rpcClient.call(i);
            System.out.println("结果为：" + res);
        }

    }
}
