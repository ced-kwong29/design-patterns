package com.csen_359.design_patterns.service.anomaly;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Runs every registered {@link AnomalyDetector} strategy and merges their
 * alerts.
 *
 * <p>Spring injects every {@code AnomalyDetector} bean into the constructor
 * list automatically, so adding a strategy needs zero changes here.
 *
 * <p>Note: this class deliberately does <b>not</b> implement
 * {@code AnomalyDetector}. If it did, Spring would try to inject it into its
 * own {@code List<AnomalyDetector>} - a self-reference that both breaks
 * construction and would recurse at runtime.
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
