package com.csen_359.design_patterns.mediator;

import com.csen_359.design_patterns.builder.AlertBuilder;
import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mediator pattern - the Concrete Mediator.
 *
 * <p>Translates subsystem events into user-visible alerts. Subsystems call
 * methods on the {@link AlertCoordinator} interface and have no reference to
 * the alert repository, the builder, or to one another.
 */
@Service
public class DefaultAlertCoordinator implements AlertCoordinator {

    private final AlertRepository alertRepository;

    public DefaultAlertCoordinator(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    @Transactional
    public void onUsageSpike(long userId, UsageCategory category,
                             double litres, double threshold) {
        alertRepository.save(AlertBuilder.builder()
                .userId(userId)
                .type(AlertType.SPIKE)
                .category(category)
                .message(String.format(
                        "%s usage of %.1f L is %.1fx your %.1f L threshold.",
                        category, litres, litres / threshold, threshold))
                .build());
    }

    @Override
    @Transactional
    public void onGoalAtRisk(long userId, long goalId, double pctConsumed) {
        alertRepository.save(AlertBuilder.builder()
                .userId(userId)
                .type(AlertType.GOAL_WARNING)
                .message(String.format(
                        "Goal #%d is at risk: %.0f%% of target already consumed.",
                        goalId, pctConsumed * 100))
                .build());
    }

    @Override
    @Transactional
    public void onGoalMissed(long userId, long goalId) {
        alertRepository.save(AlertBuilder.builder()
                .userId(userId)
                .type(AlertType.GOAL_MISSED)
                .message(String.format("Goal #%d was not met this period.", goalId))
                .build());
    }

    @Override
    @Transactional
    public void onSustainedElevation(long userId, UsageCategory category, int consecutiveDays) {
        alertRepository.save(AlertBuilder.builder()
                .userId(userId)
                .type(AlertType.SUSTAINED_ELEVATION)
                .category(category)
                .message(String.format(
                        "%s usage has been elevated for %d consecutive days.",
                        category, consecutiveDays))
                .build());
    }
}
