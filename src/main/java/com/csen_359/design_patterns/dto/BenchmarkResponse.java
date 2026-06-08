package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.UsageCategory;

/**
 * Response for GET /api/usage/benchmark the user's usage compared
 * against seeded regional reference data.
 */
public record BenchmarkResponse(
        UsageCategory category,
        double userLitresPerDay,
        double regionalLitresPerDay,
        double percentDifference,
        String message) {
}
