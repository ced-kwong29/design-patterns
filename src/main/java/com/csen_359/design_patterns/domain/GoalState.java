package com.csen_359.design_patterns.domain;

/**
 * The finite-state-machine states a {@link Goal} moves through.
 *
 * <p>Drives the <b>State</b> pattern. Transition rules live in
 * {@code GoalService} so the FSM stays explicit and independently testable:
 *
 * <pre>
 *   ACTIVE --(usage &lt; 80% of target, &gt;7 days left)--&gt; ON_TRACK
 *   ACTIVE --(usage 80-100% of target, &le;7 days left)--&gt; AT_RISK
 *   ACTIVE --(usage &gt; 100% of target)----------------&gt; MISSED
 *   ACTIVE --(period ends, usage &le; target)----------&gt; ACHIEVED
 * </pre>
 */
public enum GoalState {
    ACTIVE,
    ON_TRACK,
    AT_RISK,
    MISSED,
    ACHIEVED;

    /** Terminal states cannot transition any further. */
    public boolean isTerminal() {
        return this == MISSED || this == ACHIEVED;
    }
}
