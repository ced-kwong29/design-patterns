package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.UsageCategory;
import java.util.Map;

/**
 * Aggregate response for {@code GET /api/usage/summary}.
 */
public record UsageSummaryResponse(
        String period,
        long entryCount,
        double totalLitres,
        Map<UsageCategory, Double> litresByCategory) {
}
