package com.dekapx.apps.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping(value = "/generate", produces = "application/json")
    public String generate() {
        log.info("generating messages...");
        List<String> messages = generateMessages();

        messages.forEach(message -> {
            this.streamBridge.send(PRODUCER_BINDING_NAME, message);
        });
        return "Sending message to Kafka...";
    }

    private List<String> generateMessages() {
        return List.of(
                "{\"name\":\"Test-1\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-2\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-3\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-4\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-5\",\"email\":\"\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-6\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-7\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-8\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-9\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}",
                "{\"name\":\"Test-10\",\"email\":\"test.user@gmail.com\",\"phone\":\"1234567890\"}"
        );
    }
}
