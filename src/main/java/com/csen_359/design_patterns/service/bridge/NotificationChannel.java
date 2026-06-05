package com.csen_359.design_patterns.service.bridge;

/**
 * Bridge pattern - the Implementor interface.
 *
 * <p>Decouples notification type (the Abstraction hierarchy) from delivery
 * channel (this hierarchy). Adding a new channel — push notifications, Slack,
 * etc. — requires only a new class here. Adding a new notification type requires
 * only a new subclass of {@link Notification}. Neither side forces changes on
 * the other.
 */
public interface NotificationChannel {
    void send(long userId, String subject, String body);
}
