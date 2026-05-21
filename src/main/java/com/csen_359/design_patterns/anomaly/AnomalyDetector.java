package com.csen_359.design_patterns.anomaly;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;

/**
 * Strategy pattern - a swappable anomaly-detection algorithm.
 *
 * <p>Each implementation inspects a window of usage entries for one category
 * and returns any {@link Alert}s it would raise. New algorithms (ML-based,
 * Fourier analysis, ...) are added by writing a new {@code @Component} that
 * implements this interface - no existing code changes.
 */
public interface AnomalyDetector {

    /**
     * @param entries  usage entries already filtered to {@code category}
     * @param category the category being analysed
     * @return unsaved {@link Alert} objects for any anomalies found (never null)
     */
    List<Alert> detect(List<UsageEntry> entries, UsageCategory category);
}
