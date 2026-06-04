package com.csen_359.design_patterns.calculation;

import com.csen_359.design_patterns.domain.Season;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;

/**
 * Decorator - adjusts a wrapped calculation for the season.
 *
 * <p>Garden usage in particular is expected to be far higher in summer; this
 * decorator normalises that so comparisons across seasons stay fair.
 *
 * <pre>{@code
 * UsageCalculator calc = new SeasonalAdjustmentDecorator(
 *         new BaseUsageCalculator(), Season.SUMMER);
 * }</pre>
 */
public class SeasonalAdjustmentDecorator implements UsageCalculator {

    private final UsageCalculator delegate;
    private final Season season;

    public SeasonalAdjustmentDecorator(UsageCalculator delegate, Season season) {
        this.delegate = delegate;
        this.season = season;
    }

    @Override
    public double calculate(List<UsageEntry> entries) {
        double base = delegate.calculate(entries);
        // Normalise out the expected seasonal swing so totals stay comparable
        // across the year: summer usage is naturally higher and is scaled down,
        // winter usage is scaled up.
        return base * seasonFactor(season);
    }

    private static double seasonFactor(Season season) {
        return switch (season) {
            case SUMMER -> 0.90;        // usage runs ~10% high in summer
            case WINTER -> 1.10;        // ~10% low in winter
            case SPRING, AUTUMN -> 1.0; // treated as the neutral baseline
        };
    }
}
