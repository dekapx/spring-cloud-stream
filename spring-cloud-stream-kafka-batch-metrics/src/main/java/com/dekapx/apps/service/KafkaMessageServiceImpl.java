package com.dekapx.apps.service;

import com.dekapx.apps.common.EnrichmentException;
import com.dekapx.apps.model.Contact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class KafkaMessageServiceImpl implements KafkaMessageService {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String enrichMessage(final String message) {
        Contact contact = parseJson(message);
        if (Objects.isNull(contact.email()) || contact.email().isEmpty()) {
            log.error("-------------------- This is a faulty message --------------------");
            throw new EnrichmentException("Enrichment failed...");
        }
        log.info("-------------------- Message Enriched --------------------");
        return message;
    }

    private Contact parseJson(String message) {
        try {
            return objectMapper.readValue(message, Contact.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
