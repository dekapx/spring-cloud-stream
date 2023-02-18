package com.dekapx.apps.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMessageServiceImpl implements KafkaMessageService {
    @Override
    public String enrichMessage(final String message) {
        if (message.contains("3")) {
            log.error("-------------------- This is a faulty message --------------------");
            throw new RuntimeException();
        }
        String enrichedMessage = message + "[ Enriched ]";
        log.info(enrichedMessage);
        return enrichedMessage;
    }
}
