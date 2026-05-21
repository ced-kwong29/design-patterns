package com.csen_359.design_patterns.domain;

/**
 * Classifies why an {@link Alert} was raised.
 */
public enum AlertType {
    /** A single entry exceeded twice the category average. */
    SPIKE,
    /** A rolling average stayed above the baseline for several days. */
    SUSTAINED_ELEVATION
}
