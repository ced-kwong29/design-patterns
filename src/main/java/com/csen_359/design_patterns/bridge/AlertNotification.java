package com.csen_359.design_patterns.bridge;

import com.csen_359.design_patterns.domain.Alert;

/** Bridge pattern - Refined Abstraction: dispatches a single water usage alert. */
public class AlertNotification extends Notification {

    private final Alert alert;

    public AlertNotification(NotificationChannel channel, Alert alert) {
        super(channel);
        this.alert = alert;
    }

    @Override
    public void dispatch(long userId) {
        channel.send(userId,
                "Water Usage Alert: " + alert.getType(),
                alert.getMessage());
    }
}
