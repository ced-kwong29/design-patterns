package com.csen_359.design_patterns.calculation;

import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;

/**
 * Decorator - normalises a wrapped calculation against a region's reference
 * data so a user's usage can be compared against local averages.
 *
 * <p>Decorator order matters: seasonal adjustment is normally applied first,
 * then regional normalisation wraps it (see {@code ValidationChainConfig}'s
 * sibling comment in the plan, section 6.4).
 */
public class RegionalBenchmarkDecorator implements UsageCalculator {

    private final UsageCalculator delegate;
    private final String regionCode;

    public RegionalBenchmarkDecorator(UsageCalculator delegate, String regionCode) {
        this.delegate = delegate;
        this.regionCode = regionCode;
    }

    @Override
    public double calculate(List<UsageEntry> entries) {
        double base = delegate.calculate(entries);
        // TODO Phase 6: look up the regional benchmark for `regionCode` and
        //      express the result relative to it. Identity for now.
        return base;
    }

    public String getRegionCode() {
        return regionCode;
    }
}
