package com.csen_359.design_patterns.service.calculation;

import java.util.List;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Decorator - normalises a wrapped calculation against a region's reference
 * data so a user's usage can be compared against local averages.
 */
public class RegionalBenchmarkDecorator implements UsageCalculator {

    private final UsageCalculator delegate;
    private final String regionCode;
    private final double regionFactor;

    /** Convenience overload for an unweighted region (factor 1.0). */
    public RegionalBenchmarkDecorator(UsageCalculator delegate, String regionCode) {
        this(delegate, regionCode, 1.0);
    }

    public RegionalBenchmarkDecorator(UsageCalculator delegate, String regionCode,
                                      double regionFactor) {
        this.delegate = delegate;
        this.regionCode = regionCode;
        this.regionFactor = regionFactor > 0 ? regionFactor : 1.0;
    }

    @Override
    public double calculate(List<UsageEntry> entries) {
        return delegate.calculate(entries) * regionFactor;
    }

    public String getRegionCode() {
        return regionCode;
    }
}
