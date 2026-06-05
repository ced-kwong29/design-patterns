package com.csen_359.design_patterns.service.adapter;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Adapter pattern - the Target interface used by application code.
 *
 * <p>Application components call {@code adapt()} without knowing whether the
 * data originated from an external meter, a manual log, or any future source.
 * Swap in a new {@code ExternalMeterAdapter} implementation to support a
 * different utility vendor without touching any calling code.
 */
public interface WaterMeterAdapter {
    UsageEntry adapt(ExternalMeterReading reading);
}
