package com.csen_359.design_patterns.anomaly;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
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

    @Override
    public List<Alert> detect(List<UsageEntry> entries, UsageCategory category) {
        // TODO Phase 4: compute the 30-day average for `category`, then raise a
        //      SPIKE alert (via AlertBuilder) for any entry above
        //      SPIKE_MULTIPLIER x average. Returns empty until implemented.
        return List.of();
    }
}
