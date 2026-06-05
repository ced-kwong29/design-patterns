package com.csen_359.design_patterns.service.bridge;

import java.util.List;

/** Bridge pattern - Refined Abstraction: dispatches a multi-item periodic digest. */
public class DigestNotification extends Notification {

    private final String periodLabel;
    private final List<String> items;

    public DigestNotification(NotificationChannel channel, String periodLabel, List<String> items) {
        super(channel);
        this.periodLabel = periodLabel;
        this.items = List.copyOf(items);
    }

    @Override
    public void dispatch(long userId) {
        String body = "- " + String.join("\n- ", items);
        channel.send(userId, periodLabel + " Water Usage Digest", body);
    }
}
