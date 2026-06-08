package com.csen_359.design_patterns.service.bridge;

/**
 * Bridge pattern - the Implementor interface.
 */
public interface NotificationChannel {
    void send(long userId, String subject, String body);
}
