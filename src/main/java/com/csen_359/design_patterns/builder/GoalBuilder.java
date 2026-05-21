package com.csen_359.design_patterns.builder;

import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.domain.GoalPeriod;
import com.csen_359.design_patterns.domain.GoalState;
import com.csen_359.design_patterns.domain.UsageCategory;
import java.time.LocalDate;

/**
 * Builder pattern - fluent construction of {@link Goal}.
 *
 * <p>A new goal always starts in {@link GoalState#ACTIVE}. {@code category}
 * is optional - leaving it null produces an overall-usage goal.
 */
public final class GoalBuilder {

    private Long userId;
    private UsageCategory category;
    private double targetLitres;
    private GoalPeriod period;
    private LocalDate startsAt;
    private LocalDate endsAt;

    private GoalBuilder() {
    }

    public static GoalBuilder builder() {
        return new GoalBuilder();
    }

    public GoalBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public GoalBuilder category(UsageCategory category) {
        this.category = category;
        return this;
    }

    public GoalBuilder targetLitres(double targetLitres) {
        this.targetLitres = targetLitres;
        return this;
    }

    public GoalBuilder period(GoalPeriod period) {
        this.period = period;
        return this;
    }

    public GoalBuilder startsAt(LocalDate startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public GoalBuilder endsAt(LocalDate endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public Goal build() {
        if (userId == null) {
            throw new IllegalStateException("Goal requires a userId");
        }
        if (period == null) {
            throw new IllegalStateException("Goal requires a period");
        }
        if (startsAt == null || endsAt == null) {
            throw new IllegalStateException("Goal requires startsAt and endsAt");
        }
        if (targetLitres <= 0) {
            throw new IllegalStateException("Goal requires a positive targetLitres");
        }
        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setCategory(category);
        goal.setTargetLitres(targetLitres);
        goal.setPeriod(period);
        goal.setState(GoalState.ACTIVE);
        goal.setStartsAt(startsAt);
        goal.setEndsAt(endsAt);
        return goal;
    }
}
