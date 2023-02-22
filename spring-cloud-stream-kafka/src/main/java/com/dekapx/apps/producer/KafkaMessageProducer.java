package com.dekapx.apps.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class KafkaMessageProducer {
    @Bean
    public Supplier<String> producer() {
        return () -> " Test message from Message Producer";
    }
}
