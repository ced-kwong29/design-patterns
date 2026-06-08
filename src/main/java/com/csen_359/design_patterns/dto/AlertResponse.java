package com.csen_359.design_patterns.dto;

import java.time.LocalDateTime;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;

public record AlertResponse(
        Long id,
        Long userId,
        AlertType type,
        UsageCategory category,
        String message,
        LocalDateTime createdAt,
        boolean acknowledged) {

    public static AlertResponse from(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getUserId(),
                alert.getType(),
                alert.getCategory(),
                alert.getMessage(),
                alert.getCreatedAt(),
                alert.getAcknowledgedAt() != null);
    }
}
