package com.csen_359.design_patterns.service.validation;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Chain of Responsibility node - ensures a usage category is present.
 *
 */
public class CategoryValidationHandler extends UsageEntryHandler {

    /** A single drinking-water entry beyond this is almost certainly a misread. */
    private static final double MAX_DRINKING_LITRES = 20.0;

    @Override
    protected void validate(UsageEntry entry) {
        if (entry.getCategory() == null) {
            throw new ValidationException("category is required");
        }
        if (entry.getCategory() == UsageCategory.DRINKING
                && entry.getLitres() > MAX_DRINKING_LITRES) {
            throw new ValidationException(String.format(
                    "DRINKING entry of %.1f L exceeds the per-entry cap of %.1f L",
                    entry.getLitres(), MAX_DRINKING_LITRES));
        }
    }
}
