package com.csen_359.design_patterns.report;

import com.csen_359.design_patterns.domain.UsageCategory;
import java.time.LocalDate;
import java.util.Map;

/**
 * The output of the {@link ReportGenerator} template method - the JSON shape
 * returned by {@code GET /api/reports/weekly} and {@code /monthly}.
 */
public record Report(
        String type,
        LocalDate from,
        LocalDate to,
        double totalLitres,
        Map<UsageCategory, Double> litresByCategory,
        int anomalyCount,
        String summary) {
}
