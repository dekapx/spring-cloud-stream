package com.dekapx.apps.consumer;

import com.dekapx.apps.service.KafkaMessageService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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
    private Counter successCounter;
    private Counter failureCounter;

    private MeterRegistry meterRegistry;
    private RetryTemplate retryTemplate;
    private KafkaMessageService kafkaMessageService;

    @Autowired
    public KafkaMessageConsumer(MeterRegistry meterRegistry,
                                RetryTemplate retryTemplate,
                                KafkaMessageService kafkaMessageService) {
        this.meterRegistry = meterRegistry;
        this.retryTemplate = retryTemplate;
        this.kafkaMessageService = kafkaMessageService;

        successCounter = Counter.builder("CONSUMER_SUCCESS_MESSAGE_COUNT")
                .description("Number of message processed by Consumer...")
                .register(meterRegistry);

        failureCounter = Counter.builder("CONSUMER_FAILURE_MESSAGE_COUNT")
                .description("Number of message failed by Consumer...")
                .register(meterRegistry);
    }

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
            successCounter.increment();
            return null;
        });
        log.info("Message received: {}", msg);
    };

    @Bean
    private Consumer<ErrorMessage> consumerErrorHandler() {
        return errorMessage -> {
            failureCounter.increment();
            log.error("Exception while performing retry operation...[{}]", errorMessage.getOriginalMessage());
        };
    }
}
