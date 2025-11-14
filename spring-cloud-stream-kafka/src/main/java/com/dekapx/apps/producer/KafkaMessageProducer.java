package com.dekapx.apps.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class KafkaMessageProducer {
    @Autowired
    private MessageBuilder messageBuilder;

    @Bean
    public Supplier<String> producer() {
        log.info("[MessageProducer]: Producing message...");
        return () -> "[MessageProducer ->" + messageBuilder.buildMessage() + "]";
    }
}
