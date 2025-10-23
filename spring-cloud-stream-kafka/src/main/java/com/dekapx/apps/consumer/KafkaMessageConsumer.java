package com.dekapx.apps.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class KafkaMessageConsumer {
    @Bean
    public Consumer<String> consumer() {
        return message -> {
            log.info("KafkaMessageConsumer: Message received: [{}]", message);
        };
    }
}
