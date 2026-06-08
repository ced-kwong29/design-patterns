package com.csen_359.design_patterns.service.adapter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Adapter pattern - bridges an external utility meter's data format to the
 * application's internal {@link UsageEntry} domain object.
 */
@Component
public class ExternalMeterAdapter implements WaterMeterAdapter {

    private static final double GALLONS_TO_LITRES = 3.78541;

    private static final Map<String, UsageCategory> CODE_MAP = Map.of(
            "SHW", UsageCategory.SHOWER,
            "BTH", UsageCategory.BATH,
            "LND", UsageCategory.LAUNDRY,
            "DSH", UsageCategory.DISHWASHER,
            "GDN", UsageCategory.GARDEN,
            "DRK", UsageCategory.DRINKING
    );

    @Override
    public UsageEntry adapt(ExternalMeterReading reading) {
        UsageEntry entry = new UsageEntry();
        entry.setLitres(reading.gallons() * GALLONS_TO_LITRES);
        entry.setCategory(CODE_MAP.getOrDefault(reading.usageTypeCode(), UsageCategory.OTHER));
        entry.setLoggedAt(LocalDateTime.ofInstant(reading.readingAt(), ZoneId.systemDefault()));
        entry.setNotes("Imported from meter " + reading.deviceId());
        return entry;
    }
}
