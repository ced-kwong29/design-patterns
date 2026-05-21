package com.csen_359.design_patterns.event;

import com.csen_359.design_patterns.domain.GoalState;

/**
 * Observer pattern - published when a goal's FSM state transitions, so the
 * dashboard and digest accumulator can react.
 */
public record GoalStatusChangedEvent(
        Long goalId,
        Long userId,
        GoalState from,
        GoalState to) {
}
