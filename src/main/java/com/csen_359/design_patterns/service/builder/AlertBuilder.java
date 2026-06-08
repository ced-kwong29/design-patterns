package com.csen_359.design_patterns.service.builder;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;

/**
 * Builder pattern - fluent construction of {@link Alert}.
 *
 */
public final class AlertBuilder {

    private Long userId;
    private AlertType type;
    private UsageCategory category;
    private String message;

    private AlertBuilder() {
    }

    public static AlertBuilder builder() {
        return new AlertBuilder();
    }

    public AlertBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public AlertBuilder type(AlertType type) {
        this.type = type;
        return this;
    }

    public AlertBuilder category(UsageCategory category) {
        this.category = category;
        return this;
    }

    public AlertBuilder message(String message) {
        this.message = message;
        return this;
    }

    public Alert build() {
        if (userId == null) {
            throw new IllegalStateException("Alert requires a userId");
        }
        if (type == null) {
            throw new IllegalStateException("Alert requires a type");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalStateException("Alert requires a message");
        }
        Alert alert = new Alert();
        alert.setUserId(userId);
        alert.setType(type);
        alert.setCategory(category);
        alert.setMessage(message);
        return alert;
    }
}
