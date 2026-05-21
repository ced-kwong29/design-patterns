package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;
import java.time.LocalDateTime;

/**
 * API view of an {@link Alert}.
 */
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
