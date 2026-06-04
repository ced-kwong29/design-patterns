package com.csen_359.design_patterns.singleton;

/**
 * Singleton pattern - one process-wide instance of conservation threshold values.
 *
 * <p>Uses double-checked locking so the initialisation block runs exactly once
 * even under concurrent boot-time access. All thresholds are read-only after
 * construction; no synchronisation is needed on the getters.
 */
public final class ConservationThresholds {

    private static volatile ConservationThresholds instance;

    private final double spikeMultiplier;
    private final int sustainedElevationDays;
    private final double goalRiskThresholdPct;
    private final double criticalAlertLitresPerDay;

    private ConservationThresholds() {
        this.spikeMultiplier           = 2.5;
        this.sustainedElevationDays    = 3;
        this.goalRiskThresholdPct      = 0.85;
        this.criticalAlertLitresPerDay = 500.0;
    }

    public static ConservationThresholds getInstance() {
        if (instance == null) {
            synchronized (ConservationThresholds.class) {
                if (instance == null) {
                    instance = new ConservationThresholds();
                }
            }
        }
        return instance;
    }

    /** Usage is a spike when it exceeds this multiple of the category average. */
    public double getSpikeMultiplier() {
        return spikeMultiplier;
    }

    /** Number of consecutive days of elevated usage before raising a sustained-elevation alert. */
    public int getSustainedElevationDays() {
        return sustainedElevationDays;
    }

    /** Fraction of a goal's budget consumed at which ON_TRACK transitions to AT_RISK. */
    public double getGoalRiskThresholdPct() {
        return goalRiskThresholdPct;
    }

    /** Daily litres above which a CRITICAL alert is raised regardless of averages. */
    public double getCriticalAlertLitresPerDay() {
        return criticalAlertLitresPerDay;
    }
}
