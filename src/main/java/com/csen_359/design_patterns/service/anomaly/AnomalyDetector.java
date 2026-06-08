package com.csen_359.design_patterns.service.anomaly;

import java.util.List;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Strategy pattern - a swappable anomaly-detection algorithm.
 */
public interface AnomalyDetector {
    List<Alert> detect(List<UsageEntry> entries, UsageCategory category);
}
