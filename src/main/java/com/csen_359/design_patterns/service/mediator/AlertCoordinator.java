package com.csen_359.design_patterns.service.mediator;

import com.csen_359.design_patterns.domain.UsageCategory;

/**
 * Mediator pattern - the Mediator interface.
 */
public interface AlertCoordinator {
    void onUsageSpike(long userId, UsageCategory category, double litres, double threshold);
    void onGoalAtRisk(long userId, long goalId, double pctConsumed);
    void onGoalMissed(long userId, long goalId);
    void onSustainedElevation(long userId, UsageCategory category, int consecutiveDays);
}
