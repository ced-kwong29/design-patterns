package com.csen_359.design_patterns.service.validation;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Chain of Responsibility node - rejects physically impossible litre values.
 */
public class RangeValidationHandler extends UsageEntryHandler {

    /** No sane single entry exceeds this many litres. */
    private static final double MAX_LITRES = 10_000.0;

    /** No household fixture realistically sustains more than this flow. */
    private static final double MAX_LITRES_PER_MINUTE = 60.0;

    @Override
    protected void validate(UsageEntry entry) {
        double litres = entry.getLitres();
        if (litres < 0) {
            throw new ValidationException("litres cannot be negative: " + litres);
        }
        if (litres > MAX_LITRES) {
            throw new ValidationException(
                    "litres exceeds the plausible maximum of " + MAX_LITRES + ": " + litres);
        }

        Integer duration = entry.getDurationMinutes();
        if (duration != null && duration > 0) {
            double flowRate = litres / duration;
            if (flowRate > MAX_LITRES_PER_MINUTE) {
                throw new ValidationException(String.format(
                        "implausible flow rate: %.1f L over %d min is %.1f L/min (max %.1f)",
                        litres, duration, flowRate, MAX_LITRES_PER_MINUTE));
            }
        }
    }
}
