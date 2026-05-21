package com.csen_359.design_patterns.validation;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Chain of Responsibility node - rejects physically impossible litre values.
 */
public class RangeValidationHandler extends UsageEntryHandler {

    /** No sane single entry exceeds this many litres. */
    private static final double MAX_LITRES = 10_000.0;

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
        // TODO: optionally validate durationMinutes against litres for a
        //       plausible flow rate once thresholds are tuned.
    }
}
