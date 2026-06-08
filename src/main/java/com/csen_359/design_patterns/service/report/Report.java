package com.csen_359.design_patterns.service.report;

import java.time.LocalDate;
import java.util.Map;

import com.csen_359.design_patterns.domain.UsageCategory;

public record Report(
        String type,
        LocalDate from,
        LocalDate to,
        double totalLitres,
        Map<UsageCategory, Double> litresByCategory,
        int anomalyCount,
        String summary) {
}
