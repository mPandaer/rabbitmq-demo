package com.pandaer.examples.loggerv3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderLogProvider extends LogProvider{
    @Override
    String getLogSource() {
        return "order";
    }

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        OrderLogProvider orderLogProvider = new OrderLogProvider();
        orderLogProvider.sendLog(10,2000);
    }
}
