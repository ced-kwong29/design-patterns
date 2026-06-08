package com.csen_359.design_patterns.service.adapter;

import java.time.Instant;

/**
 * Adapter pattern - the Adaptee's data model.
 */
public record ExternalMeterReading(
        String deviceId,
        String usageTypeCode,
        double gallons,
        Instant readingAt,
        String accountNumber
) {}
