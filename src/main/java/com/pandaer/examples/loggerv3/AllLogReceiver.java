package com.pandaer.examples.loggerv3;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class AllLogReceiver extends LogReceiver{
    @Override
    List<String> getBindingKeys() {
        return Collections.singletonList("#");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        AllLogReceiver allLogReceiver = new AllLogReceiver();
        allLogReceiver.receiveLog();
    }
}
