package com.csen_359.design_patterns.calculation;

import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;

/**
 * Decorator pattern - the component interface for usage calculation.
 *
 * <p>{@code BaseUsageCalculator} provides the raw sum; decorators wrap it to
 * layer on seasonal and regional adjustments. Callers depend only on this
 * interface and never know which decorators are active.
 */
public interface UsageCalculator {

    /** Returns the (possibly adjusted) total litres for the given entries. */
    double calculate(List<UsageEntry> entries);
}
