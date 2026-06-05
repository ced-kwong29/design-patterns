package com.csen_359.design_patterns.service.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Bridge pattern - concrete Implementor: SMS delivery. */
@Component
public class SmsChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(SmsChannel.class);

    @Override
    public void send(long userId, String subject, String body) {
        log.info("[SMS] userId={} | {}: {}", userId, subject, body);
    }
}
