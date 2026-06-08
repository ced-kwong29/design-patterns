package com.csen_359.design_patterns.service.report;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.anomaly.CompositeDetector;

/**
 * Template Method pattern - defines the fixed skeleton of report generation:
 */
public abstract class ReportGenerator implements ReportProvider {

    protected final UsageEntryRepository usageEntryRepository;
    protected final CompositeDetector compositeDetector;

    protected ReportGenerator(UsageEntryRepository usageEntryRepository,
                              CompositeDetector compositeDetector) {
        this.usageEntryRepository = usageEntryRepository;
        this.compositeDetector = compositeDetector;
    }

    /** The template method - the invariant algorithm. Do not override. */
    public final Report generate(Long userId) {
        LocalDate from = windowStart();
        LocalDate to = windowEnd();
        List<UsageEntry> entries = gatherData(userId, from, to);
        double total = computeTotal(entries);
        Map<UsageCategory, Double> byCategory = computeByCategory(entries);
        int anomalies = detectAnomalies(entries);
        return format(from, to, total, byCategory, anomalies);
    }

    /** A short label, e.g. "WEEKLY" or "MONTHLY". */
    protected abstract String reportType();

    /** First day of the reporting window (inclusive). */
    protected abstract LocalDate windowStart();

    /** Last day of the reporting window (inclusive). */
    protected abstract LocalDate windowEnd();


    protected List<UsageEntry> gatherData(Long userId, LocalDate from, LocalDate to) {
        return usageEntryRepository.findByUserIdAndLoggedAtBetween(
                userId, from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    protected double computeTotal(List<UsageEntry> entries) {
        return entries.stream().mapToDouble(UsageEntry::getLitres).sum();
    }

    protected Map<UsageCategory, Double> computeByCategory(List<UsageEntry> entries) {
        return entries.stream().collect(Collectors.groupingBy(
                UsageEntry::getCategory,
                Collectors.summingDouble(UsageEntry::getLitres)));
    }

    protected int detectAnomalies(List<UsageEntry> entries) {
        // Run every detector strategy per category over the window and tally hits.
        Map<UsageCategory, List<UsageEntry>> byCategory = entries.stream()
                .collect(Collectors.groupingBy(UsageEntry::getCategory));
        int count = 0;
        for (Map.Entry<UsageCategory, List<UsageEntry>> e : byCategory.entrySet()) {
            count += compositeDetector.detectAll(e.getValue(), e.getKey()).size();
        }
        return count;
    }

    protected Report format(LocalDate from, LocalDate to, double total,
                            Map<UsageCategory, Double> byCategory, int anomalies) {
        String topCategory = byCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> String.format("%s (%.1f L)", e.getKey(), e.getValue()))
                .orElse("no usage");

        String summary = String.format(
                "%s report (%s to %s): %.1f L across %d categor%s. Top use: %s. %s.",
                reportType(), from, to, total, byCategory.size(),
                byCategory.size() == 1 ? "y" : "ies",
                topCategory,
                anomalies == 0
                        ? "No anomalies detected"
                        : anomalies + " anomal" + (anomalies == 1 ? "y" : "ies") + " flagged");

        return new Report(reportType(), from, to, total, byCategory, anomalies, summary);
    }
}
