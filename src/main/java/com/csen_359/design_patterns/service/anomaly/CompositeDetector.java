package com.csen_359.design_patterns.service.anomaly;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Runs every registered {@link AnomalyDetector} strategy and merges their
 * alerts
 */
@Service
public class CompositeDetector {

    private static final Logger log = LoggerFactory.getLogger(CompositeDetector.class);

    private final List<AnomalyDetector> detectors;

    public CompositeDetector(List<AnomalyDetector> detectors) {
        this.detectors = detectors;
        log.info("CompositeDetector wired with {} strategy(ies): {}",
                detectors.size(),
                detectors.stream().map(d -> d.getClass().getSimpleName()).toList());
    }

    /** Delegates to every strategy and concatenates the alerts they raise. */
    public List<Alert> detectAll(List<UsageEntry> entries, UsageCategory category) {
        List<Alert> merged = new ArrayList<>();
        for (AnomalyDetector detector : detectors) {
            merged.addAll(detector.detect(entries, category));
        }
        return merged;
    }
}
