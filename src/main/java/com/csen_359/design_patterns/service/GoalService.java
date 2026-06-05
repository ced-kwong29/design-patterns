package com.csen_359.design_patterns.service;

import com.csen_359.design_patterns.service.builder.GoalBuilder;
import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.domain.GoalState;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.dto.CreateGoalRequest;
import com.csen_359.design_patterns.event.GoalStatusChangedEvent;
import com.csen_359.design_patterns.event.UsageLoggedEvent;
import com.csen_359.design_patterns.repository.GoalRepository;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages conservation goals and the State-pattern FSM that drives their
 * {@link GoalState}. Transition rules are kept here, explicit and isolated
 * from the rest of the business logic, so they can be unit tested directly.
 */
@Service
public class GoalService {

    private static final Logger log = LoggerFactory.getLogger(GoalService.class);

    private final GoalRepository goalRepository;
    private final UsageEntryRepository usageEntryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public GoalService(GoalRepository goalRepository,
                       UsageEntryRepository usageEntryRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.goalRepository = goalRepository;
        this.usageEntryRepository = usageEntryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Goal createGoal(CreateGoalRequest request) {
        Goal goal = GoalBuilder.builder()
                .userId(request.userId())
                .category(request.category())
                .targetLitres(request.targetLitres())
                .period(request.period())
                .startsAt(request.startsAt())
                .endsAt(request.endsAt())
                .build();
        return goalRepository.save(goal);
    }

    @Transactional(readOnly = true)
    public List<Goal> listGoals(Long userId) {
        return goalRepository.findByUserId(userId);
    }

    /**
     * Progress as a percentage of the target litres consumed so far.
     */
    @Transactional(readOnly = true)
    public double progressPercent(Goal goal) {
        if (goal.getTargetLitres() <= 0) {
            return 0.0;
        }
        LocalDateTime from = goal.getStartsAt().atStartOfDay();
        LocalDateTime to = goal.getEndsAt().atTime(LocalTime.MAX);

        // A null category means the goal covers overall usage.
        List<UsageEntry> entries = (goal.getCategory() == null)
                ? usageEntryRepository.findByUserIdAndLoggedAtBetween(goal.getUserId(), from, to)
                : usageEntryRepository.findByUserIdAndCategoryAndLoggedAtBetween(
                        goal.getUserId(), goal.getCategory(), from, to);

        double consumed = entries.stream().mapToDouble(UsageEntry::getLitres).sum();
        return consumed / goal.getTargetLitres() * 100.0;
    }

    /**
     * Observer hook - re-evaluates goals affected by a freshly logged entry.
     */
    @Transactional
    public void applyUsage(UsageLoggedEvent event) {
        List<Goal> affected = goalRepository.findByUserId(event.userId()).stream()
                .filter(goal -> !goal.getState().isTerminal())
                .filter(goal -> goal.getCategory() == null || goal.getCategory() == event.category())
                .toList();
        affected.forEach(this::recalculateState);
        log.info("applyUsage() re-evaluated {} goal(s) for user {}",
                affected.size(), event.userId());
    }

    /**
     * Entry point for {@code GoalStatusRecalcJob} - recompute every active
     * goal's state.
     */
    @Transactional
    public void recalculateAll() {
        List<Goal> active = goalRepository.findByStateNot(GoalState.ACHIEVED);
        active.forEach(this::recalculateState);
    }

    /**
     * Recomputes a single goal's state and emits a {@link GoalStatusChangedEvent}
     * if it transitioned.
     */
    @Transactional
    public void recalculateState(Goal goal) {
        GoalState previous = goal.getState();
        GoalState next = nextState(goal);
        if (next != previous) {
            goal.setState(next);
            goalRepository.save(goal);
            eventPublisher.publishEvent(new GoalStatusChangedEvent(
                    goal.getId(), goal.getUserId(), previous, next));
        }
    }

    /**
     * The State-pattern transition function. See {@link GoalState} for the
     * full transition table.
     */
    GoalState nextState(Goal goal) {
        if (goal.getState().isTerminal()) {
            return goal.getState();
        }

        double progress = progressPercent(goal);
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndsAt());

        // Over the litre budget - the conservation goal can no longer be met.
        if (progress > 100.0) {
            return GoalState.MISSED;
        }
        // Period has elapsed while still within budget - the goal succeeded.
        if (daysLeft < 0) {
            return GoalState.ACHIEVED;
        }
        // Nearing the budget with little time to spare.
        if (progress >= 80.0 && daysLeft <= 7) {
            return GoalState.AT_RISK;
        }
        // Comfortable margin remaining.
        return GoalState.ON_TRACK;
    }
}
