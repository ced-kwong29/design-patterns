package com.csen_359.design_patterns.adapter;

import java.time.Instant;

/**
 * Adapter pattern - the Adaptee's data model.
 *
 * <p>Represents a raw reading from an external water-utility meter API:
 * gallons instead of litres, Unix-epoch timestamps, and proprietary
 * usage-type codes that don't map 1-to-1 to {@link com.csen_359.design_patterns.domain.UsageCategory}.
 */
public record ExternalMeterReading(
        String deviceId,
        String usageTypeCode,
        double gallons,
        Instant readingAt,
        String accountNumber
) {}
