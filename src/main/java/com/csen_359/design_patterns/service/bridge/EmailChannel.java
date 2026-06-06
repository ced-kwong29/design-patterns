package com.csen_359.design_patterns.service.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Bridge pattern - concrete Implementor: email delivery. */
@Component
@Primary
public class EmailChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(EmailChannel.class);

    @Override
    public void send(long userId, String subject, String body) {
        log.info("[EMAIL] userId={} | Subject: {} | Body: {}", userId, subject, body);
    }
}
