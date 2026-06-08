package com.csen_359.design_patterns.domain;

/**
 * Classifies why an Aler was raised.
 */
public enum AlertType {
    /** A single entry exceeded twice the category average. */
    SPIKE,
    /** A rolling average stayed above the baseline for several days. */
    SUSTAINED_ELEVATION,
    /** A goal is consuming budget faster than the period allows. */
    GOAL_WARNING,
    /** A goal period ended without reaching target. */
    GOAL_MISSED
}
