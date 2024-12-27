package com.pandaer.examples.loggerv3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UserLogProvider extends LogProvider{
    @Override
    String getLogSource() {
        return "user";
    }

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        UserLogProvider userLogProvider = new UserLogProvider();
        userLogProvider.sendLog(20,3000);
    }

}
