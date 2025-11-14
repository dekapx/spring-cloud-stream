package com.dekapx.apps.producer;

import org.springframework.stereotype.Component;

@Component
public class MessageBuilder {
    public String buildMessage() {
        return "[MessageBuilder] -> Test message";
    }
}

