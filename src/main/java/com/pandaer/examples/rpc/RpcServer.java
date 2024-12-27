package com.pandaer.examples.rpc;

import com.pandaer.utils.MqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.pandaer.constants.QueueNameConstant.RPC_QUEUE;

public class RpcServer {


    /**
     * 提供的服务
     * @param num
     * @return
     */
    int fibo(int num) {
        if (num == 0) return 0;
        if (num == 1) return 1;
        return fibo(num-1) + fibo(num - 2);
    }


    // 响应消息
    public void response() throws IOException, TimeoutException {
        Connection connect = MqUtil.getMqConnect("localhost");
        Channel channel = connect.createChannel();

        // 确保队列存在
        channel.queueDeclare(RPC_QUEUE,false,false,false,null);

        // 接收请求
        channel.basicConsume(RPC_QUEUE,(consumerTag, message) -> {
            int value = Integer.parseInt(new String(message.getBody(), StandardCharsets.UTF_8));
            int result = fibo(value);
            // 将结果发送出去
            String replyQueueName = message.getProperties().getReplyTo();
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                    .correlationId(message.getProperties().getCorrelationId())
                    .build();
            channel.basicPublish("",replyQueueName,basicProperties,String.valueOf(result).getBytes(StandardCharsets.UTF_8));
        },consumerTag -> {});
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        RpcServer rpcServer = new RpcServer();

        // 提供服务
        rpcServer.response();
    }

}
