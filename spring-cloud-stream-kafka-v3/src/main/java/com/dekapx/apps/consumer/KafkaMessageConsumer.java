package com.dekapx.apps.consumer;

import com.dekapx.apps.common.EnrichmentException;
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
    private RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .fixedBackoff(1000)
            .retryOn(RuntimeException.class)
            .build();
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
        retryTemplate.execute(retryContext ->
                this.kafkaMessageService.enrichMessage(msg));
        if (msg.contains("3")) {
            throw new EnrichmentException("Faulty Message...");
        }
        log.info("Message received: {}", msg);
    };

    @Bean
    private Consumer<ErrorMessage> consumerErrorHandler() {
        return errorMessage -> {
            log.error("Exception while performing retry operation...[{}]", errorMessage.getOriginalMessage());
        };
    }
}
