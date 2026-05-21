package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.time.LocalDateTime;

/**
 * API view of a {@link UsageEntry}.
 */
public record UsageEntryResponse(
        Long id,
        Long userId,
        UsageCategory category,
        double litres,
        Integer durationMinutes,
        LocalDateTime loggedAt,
        String notes,
        Double adjustedLitres) {

    public static UsageEntryResponse from(UsageEntry entry) {
        return new UsageEntryResponse(
                entry.getId(),
                entry.getUserId(),
                entry.getCategory(),
                entry.getLitres(),
                entry.getDurationMinutes(),
                entry.getLoggedAt(),
                entry.getNotes(),
                entry.getAdjustedLitres());
    }
}
