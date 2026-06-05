package com.csen_359.design_patterns.service.calculation;

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
    private final double regionFactor;

    /** Convenience overload for an unweighted region (factor 1.0). */
    public RegionalBenchmarkDecorator(UsageCalculator delegate, String regionCode) {
        this(delegate, regionCode, 1.0);
    }

    /**
     * @param regionFactor multiplier that re-expresses the wrapped total in
     *        terms of a reference region (e.g. {@code referenceAvg / regionAvg}),
     *        so a litre in a water-scarce region weighs more than one in a
     *        water-rich region. The caller computes it from seeded benchmark
     *        data; {@code 1.0} leaves the total unchanged.
     */
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
