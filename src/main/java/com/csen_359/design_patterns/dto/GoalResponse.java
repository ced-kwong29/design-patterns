package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.domain.GoalPeriod;
import com.csen_359.design_patterns.domain.GoalState;
import com.csen_359.design_patterns.domain.UsageCategory;
import java.time.LocalDate;

/**
 * API view of a {@link Goal}, including its current FSM state and progress.
 */
public record GoalResponse(
        Long id,
        Long userId,
        UsageCategory category,
        double targetLitres,
        GoalPeriod period,
        GoalState state,
        LocalDate startsAt,
        LocalDate endsAt,
        double progressPercent) {

    public static GoalResponse from(Goal goal, double progressPercent) {
        return new GoalResponse(
                goal.getId(),
                goal.getUserId(),
                goal.getCategory(),
                goal.getTargetLitres(),
                goal.getPeriod(),
                goal.getState(),
                goal.getStartsAt(),
                goal.getEndsAt(),
                progressPercent);
    }
}
