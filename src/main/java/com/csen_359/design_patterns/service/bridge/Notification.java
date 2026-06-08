package com.csen_359.design_patterns.service.bridge;

/**
 * Bridge pattern - the Abstraction.
 */
public abstract class Notification {

    protected final NotificationChannel channel;

    protected Notification(NotificationChannel channel) {
        this.channel = channel;
    }

    public abstract void dispatch(long userId);
}
