package com.dekapx.apps.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

import static com.dekapx.apps.common.CommonConstants.PRODUCER_BINDING_NAME;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApplicationController {
    @Autowired
    private StreamBridge streamBridge;

    @GetMapping(value = "/info", produces = "application/json")
    public String getInfo() {
        log.info("ApplicationController.getInfo() invoked...");
        return "Spring Cloud Stream Kafka v2...";
    }

    @GetMapping(value = "/generate/{count}", produces = "application/json")
    public String generate(@PathVariable Integer count) {
        log.info("generating messages...");

        IntStream.rangeClosed(1, count).forEach(i -> {
            this.streamBridge.send(PRODUCER_BINDING_NAME,"StreamBridge - Test Message #" + i);
        });

        return "Sending message to Kafka...";
    }
}
