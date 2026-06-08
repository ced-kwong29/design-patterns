package com.csen_359.design_patterns.dto;

import java.time.LocalDate;

import com.csen_359.design_patterns.domain.GoalPeriod;
import com.csen_359.design_patterns.domain.UsageCategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request body for POST /api/goals
 */
public record CreateGoalRequest(
        @NotNull Long userId,
        UsageCategory category,
        @Positive double targetLitres,
        @NotNull GoalPeriod period,
        @NotNull LocalDate startsAt,
        @NotNull LocalDate endsAt) {
}
