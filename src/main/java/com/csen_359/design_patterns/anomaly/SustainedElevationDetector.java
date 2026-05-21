package com.csen_359.design_patterns.anomaly;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;
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
        // TODO Phase 4: build a 3-day rolling average and compare it to the
        //      30-day baseline; raise a SUSTAINED_ELEVATION alert when the
        //      ratio exceeds ELEVATION_THRESHOLD. Returns empty until done.
        return List.of();
    }
}
