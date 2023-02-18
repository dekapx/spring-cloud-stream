package com.dekapx.apps.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class KafkaMessageConsumer {
    @Bean
    public Consumer<List<String>> consumer() {
        return messages -> {
            log.info("------------------ Collection size [{}] ------------------", messages.size());
            messages.forEach(processMessage);
        };
    }

    private Consumer<String> processMessage = (msg) -> {
        log.info("Message received {}", msg);
    };
}
