package com.csen_359.design_patterns.report;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Template Method pattern - defines the fixed skeleton of report generation:
 *
 * <pre>
 *   gather data -&gt; compute total -&gt; break down by category
 *               -&gt; detect anomalies -&gt; format
 * </pre>
 *
 * <p>{@link #generate(Long)} is {@code final} so the order never changes.
 * Subclasses fill in only the steps that differ between report kinds - chiefly
 * the date window via {@link #windowStart()} / {@link #windowEnd()}. Shared
 * steps have a default implementation here but remain overridable.
 */
public abstract class ReportGenerator {

    protected final UsageEntryRepository usageEntryRepository;

    protected ReportGenerator(UsageEntryRepository usageEntryRepository) {
        this.usageEntryRepository = usageEntryRepository;
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

    // --- Primitive operations: each subclass must supply these -------------

    /** A short label, e.g. "WEEKLY" or "MONTHLY". */
    protected abstract String reportType();

    /** First day of the reporting window (inclusive). */
    protected abstract LocalDate windowStart();

    /** Last day of the reporting window (inclusive). */
    protected abstract LocalDate windowEnd();

    // --- Hook methods: shared defaults, overridable when a kind differs ----

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
        // TODO Phase 8: run the anomaly detectors over the window and count hits.
        return 0;
    }

    protected Report format(LocalDate from, LocalDate to, double total,
                            Map<UsageCategory, Double> byCategory, int anomalies) {
        // TODO Phase 8: build a human-readable narrative (top category,
        //      comparison to the prior period, goal outcomes).
        String summary = reportType() + " report: " + total + " L across "
                + byCategory.size() + " categories.";
        return new Report(reportType(), from, to, total, byCategory, anomalies, summary);
    }
}
