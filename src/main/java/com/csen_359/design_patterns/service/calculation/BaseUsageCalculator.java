package com.csen_359.design_patterns.service.calculation;

import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;

/**
 * Decorator pattern - the concrete component: a plain sum of raw litres,
 * with no adjustment applied.
 */
public class BaseUsageCalculator implements UsageCalculator {

    @Override
    public double calculate(List<UsageEntry> entries) {
        return entries.stream()
                .mapToDouble(UsageEntry::getLitres)
                .sum();
    }
}
