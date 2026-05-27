package com.csen_359.design_patterns.mediator;

import com.csen_359.design_patterns.domain.UsageCategory;

/**
 * Mediator pattern - the Mediator interface.
 *
 * <p>Usage, goal, and anomaly subsystems notify this coordinator when something
 * significant happens. The coordinator decides what alerts to raise and how to
 * route them, eliminating direct dependencies between subsystems. Each subsystem
 * only knows about this interface — not about each other.
 */
public interface AlertCoordinator {
    void onUsageSpike(long userId, UsageCategory category, double litres, double threshold);
    void onGoalAtRisk(long userId, long goalId, double pctConsumed);
    void onGoalMissed(long userId, long goalId);
    void onSustainedElevation(long userId, UsageCategory category, int consecutiveDays);
}
