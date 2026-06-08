package com.csen_359.design_patterns.service.adapter;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Adapter pattern - the Target interface used by application code.
 */
public interface WaterMeterAdapter {
    UsageEntry adapt(ExternalMeterReading reading);
}
