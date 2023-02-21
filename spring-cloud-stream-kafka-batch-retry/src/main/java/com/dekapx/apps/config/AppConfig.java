package com.dekapx.apps.config;

import com.dekapx.apps.common.EnrichmentException;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class AppConfig {
    @Value("${app.kafka.retry.maxAttempts}")
    private Integer maxAttempts;

    @Value("${app.kafka.retry.fixedBackoff}")
    private Integer fixedBackoff;

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(maxAttempts)
                .fixedBackoff(fixedBackoff)
                .retryOn(EnrichmentException.class)
                .build();
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
        return registry -> registry.config().commonTags("application", "Spring Cloud Stream Kafka");
    }
}
