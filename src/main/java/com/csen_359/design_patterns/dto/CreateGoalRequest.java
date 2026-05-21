package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.GoalPeriod;
import com.csen_359.design_patterns.domain.UsageCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Request body for {@code POST /api/goals}. A {@code null} category means the
 * goal covers overall usage.
 */
public record CreateGoalRequest(
        @NotNull Long userId,
        UsageCategory category,
        @Positive double targetLitres,
        @NotNull GoalPeriod period,
        @NotNull LocalDate startsAt,
        @NotNull LocalDate endsAt) {
}
