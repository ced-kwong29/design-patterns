package com.csen_359.design_patterns.dto;

import java.time.LocalDateTime;

import com.csen_359.design_patterns.domain.UsageCategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record LogUsageRequest(
        @NotNull Long userId,
        @NotNull UsageCategory category,
        @Positive double litres,
        @PositiveOrZero Integer durationMinutes,
        @NotNull LocalDateTime loggedAt,
        @Size(max = 500) String notes) {
}
