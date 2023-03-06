package com.dekapx.apps.consumer;

import com.dekapx.apps.service.KafkaMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class KafkaMessageConsumer {
    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Bean
    public Consumer<List<String>> consumer() {
        return messages -> {
            log.info("------------------ Collection size [{}] ------------------", messages.size());
            messages.forEach(processMessage);
        };
    }

    private Consumer<String> processMessage = (msg) -> {
        this.retryTemplate.execute(retryContext -> {
            this.kafkaMessageService.enrichMessage(msg);
            return null;
        });
        log.info("Message received: {}", msg);
    };

    @Bean
    private Consumer<ErrorMessage> consumerErrorHandler() {
        return errorMessage -> {
            log.error("Exception while performing retry operation...[{}]", errorMessage.getOriginalMessage());
        };
    }
}
