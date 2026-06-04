package com.csen_359.design_patterns.anomaly;

import com.csen_359.design_patterns.builder.AlertBuilder;
import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Strategy - flags a creeping increase: a 3-day rolling average sitting above
 * 150% of the 30-day baseline.
 */
@Component
public class SustainedElevationDetector implements AnomalyDetector {

    /** Rolling-average window, in days. */
    static final int ROLLING_WINDOW_DAYS = 3;

    /** A rolling average above this multiple of the baseline is "sustained". */
    static final double ELEVATION_THRESHOLD = 1.5;

    @Override
    public List<Alert> detect(List<UsageEntry> entries, UsageCategory category) {
        // Collapse the window into per-day totals (sorted oldest -> newest).
        Map<LocalDate, Double> dailyTotals = entries.stream().collect(Collectors.groupingBy(
                e -> e.getLoggedAt().toLocalDate(),
                TreeMap::new,
                Collectors.summingDouble(UsageEntry::getLitres)));

        // Need a full rolling window plus some history to form a baseline.
        if (dailyTotals.size() < ROLLING_WINDOW_DAYS + 1) {
            return List.of();
        }

        double baseline = dailyTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        if (baseline <= 0.0) {
            return List.of();
        }

        List<Double> totals = List.copyOf(dailyTotals.values());
        double rolling = totals.subList(totals.size() - ROLLING_WINDOW_DAYS, totals.size()).stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double ratio = rolling / baseline;
        if (ratio <= ELEVATION_THRESHOLD) {
            return List.of();
        }

        Alert alert = AlertBuilder.builder()
                .userId(entries.get(0).getUserId())
                .type(AlertType.SUSTAINED_ELEVATION)
                .category(category)
                .message(String.format(
                        "Sustained elevation for %s: the last %d-day average (%.1f L/day) is %.0f%% of"
                                + " the 30-day baseline (%.1f L/day).",
                        category, ROLLING_WINDOW_DAYS, rolling, ratio * 100, baseline))
                .build();
        return List.of(alert);
    }
}
