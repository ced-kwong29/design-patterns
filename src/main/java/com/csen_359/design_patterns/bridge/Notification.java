package com.csen_359.design_patterns.bridge;

/**
 * Bridge pattern - the Abstraction.
 *
 * <p>Each subclass composes its own content and delegates actual delivery to
 * the injected {@link NotificationChannel}. The two hierarchies evolve
 * independently: this side owns "what to say", the channel side owns "how to
 * deliver it".
 */
public abstract class Notification {

    protected final NotificationChannel channel;

    protected Notification(NotificationChannel channel) {
        this.channel = channel;
    }

    public abstract void dispatch(long userId);
}
