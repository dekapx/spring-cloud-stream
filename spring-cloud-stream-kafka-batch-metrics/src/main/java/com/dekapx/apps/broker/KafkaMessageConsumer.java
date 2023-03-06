package com.dekapx.apps.broker;

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

import static com.dekapx.apps.common.CommonConstants.FAILURE_MESSAGES_COUNTER_DESC;
import static com.dekapx.apps.common.CommonConstants.FAILURE_MESSAGES_COUNTER_NAME;
import static com.dekapx.apps.common.CommonConstants.SUCCESS_MESSAGES_COUNTER_DESC;
import static com.dekapx.apps.common.CommonConstants.SUCCESS_MESSAGES_COUNTER_NAME;

@Slf4j
@Component
public class KafkaMessageConsumer {
    private Counter successCounter;
    private Counter failureCounter;

    private MeterRegistry meterRegistry;
    private RetryTemplate retryTemplate;
    private KafkaMessageService kafkaMessageService;

    @Autowired
    public KafkaMessageConsumer(final MeterRegistry meterRegistry,
                                final RetryTemplate retryTemplate,
                                final KafkaMessageService kafkaMessageService) {
        this.meterRegistry = meterRegistry;
        this.retryTemplate = retryTemplate;
        this.kafkaMessageService = kafkaMessageService;

        this.successCounter = buildCounter(SUCCESS_MESSAGES_COUNTER_NAME,
                SUCCESS_MESSAGES_COUNTER_DESC);

        this.failureCounter = buildCounter(FAILURE_MESSAGES_COUNTER_NAME,
                FAILURE_MESSAGES_COUNTER_DESC);
    }

    private Counter buildCounter(final String name, final String description) {
        return Counter.builder(name)
                .description(description)
                .register(this.meterRegistry);
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
