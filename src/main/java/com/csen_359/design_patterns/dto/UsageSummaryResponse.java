package com.csen_359.design_patterns.dto;

import java.util.Map;

import com.csen_359.design_patterns.domain.UsageCategory;

public record UsageSummaryResponse(
        String period,
        long entryCount,
        double totalLitres,
        Map<UsageCategory, Double> litresByCategory) {
}
