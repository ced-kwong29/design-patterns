package com.csen_359.design_patterns.domain;

public enum GoalState {
    ACTIVE,
    ON_TRACK,
    AT_RISK,
    MISSED,
    ACHIEVED;

    public boolean isTerminal() {
        return this == MISSED || this == ACHIEVED;
    }
}
