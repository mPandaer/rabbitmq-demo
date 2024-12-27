package com.pandaer.examples.loggerv3;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ErrorOrUserLogReceiver extends LogReceiver{
    @Override
    List<String> getBindingKeys() {
        return Arrays.asList("error.*","*.user");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ErrorOrUserLogReceiver errorOrUserLogReceiver = new ErrorOrUserLogReceiver();
        errorOrUserLogReceiver.receiveLog();
    }
}
