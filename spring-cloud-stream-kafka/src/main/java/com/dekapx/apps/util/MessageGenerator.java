package com.dekapx.apps.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.dekapx.apps.common.CommonConstants.PRODUCER_BINDING_NAME;

@Component
public class MessageGenerator {
    private AtomicInteger counter = new AtomicInteger();
    @Autowired
    private StreamBridge streamBridge;

    @Scheduled(cron = "*/5 * * * * *")
    public void generateAndSendMessages() {
        IntStream.rangeClosed(1, 5).forEach(i -> {
            this.streamBridge.send(PRODUCER_BINDING_NAME,"MessageGenerator -> Test message#" + counter.incrementAndGet());
        });
    }
}
