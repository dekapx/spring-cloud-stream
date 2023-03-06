package com.dekapx.apps.broker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class KafkaMessageProducer {
    @Bean
    public Supplier<String> producer() {
        return () -> "{\"name\":\"Dummy User\",\"email\":\"dummy.user@google.com\",\"phone\":\"+1 123 456 7890\"}";
    }
}
