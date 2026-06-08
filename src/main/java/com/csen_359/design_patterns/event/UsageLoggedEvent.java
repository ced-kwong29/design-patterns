package com.csen_359.design_patterns.event;

import java.time.LocalDateTime;

import com.csen_359.design_patterns.domain.UsageCategory;

/**
 * Observer pattern - published after a usage entry is saved. 
 */
public record UsageLoggedEvent(
        Long entryId,
        Long userId,
        UsageCategory category,
        double litres,
        LocalDateTime loggedAt) {
}
