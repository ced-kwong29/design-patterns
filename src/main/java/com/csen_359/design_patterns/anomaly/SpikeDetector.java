package com.csen_359.design_patterns.anomaly;

import com.csen_359.design_patterns.builder.AlertBuilder;
import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.AlertType;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Strategy - flags a single entry whose litres exceed twice the 30-day
 * category average (a sudden spike, often a leak or a misread).
 */
@Component
public class SpikeDetector implements AnomalyDetector {

    /** An entry above this multiple of the average is treated as a spike. */
    static final double SPIKE_MULTIPLIER = 2.0;

    /** A baseline drawn from fewer entries than this is too noisy to trust. */
    static final int MIN_SAMPLE = 3;

    @Override
    public List<Alert> detect(List<UsageEntry> entries, UsageCategory category) {
        if (entries.size() < MIN_SAMPLE) {
            return List.of();
        }

        double average = entries.stream()
                .mapToDouble(UsageEntry::getLitres)
                .average()
                .orElse(0.0);
        if (average <= 0.0) {
            return List.of();
        }

        double threshold = SPIKE_MULTIPLIER * average;
        List<Alert> alerts = new ArrayList<>();
        for (UsageEntry entry : entries) {
            if (entry.getLitres() > threshold) {
                alerts.add(AlertBuilder.builder()
                        .userId(entry.getUserId())
                        .type(AlertType.SPIKE)
                        .category(category)
                        .message(String.format(
                                "Spike detected for %s: %.1f L is over %.1fx the 30-day average of %.1f L.",
                                category, entry.getLitres(), SPIKE_MULTIPLIER, average))
                        .build());
            }
        }
        return alerts;
    }
}
