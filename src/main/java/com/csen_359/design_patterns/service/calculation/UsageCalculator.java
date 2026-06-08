package com.csen_359.design_patterns.service.calculation;

import java.util.List;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Decorator pattern - the component interface for usage calculation
 */
public interface UsageCalculator {

    /** Returns the (possibly adjusted) total litres for the given entries. */
    double calculate(List<UsageEntry> entries);
}
