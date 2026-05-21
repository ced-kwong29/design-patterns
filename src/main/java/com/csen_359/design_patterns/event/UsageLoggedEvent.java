package com.csen_359.design_patterns.event;

import com.csen_359.design_patterns.domain.UsageCategory;
import java.time.LocalDateTime;

/**
 * Observer pattern - published by {@code UsageService} after a usage entry is
 * saved. Multiple listeners react independently; none affect the write path.
 */
public record UsageLoggedEvent(
        Long entryId,
        Long userId,
        UsageCategory category,
        double litres,
        LocalDateTime loggedAt) {
}
