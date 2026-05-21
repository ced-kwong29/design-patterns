package com.csen_359.design_patterns.event;

import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;

/**
 * Observer pattern - published when an anomaly detector raises an alert.
 * Drives alert persistence and the real-time SSE/WebSocket broadcast.
 */
public record AnomalyDetectedEvent(
        Long alertId,
        Long userId,
        AlertType type,
        UsageCategory category,
        String message) {
}
