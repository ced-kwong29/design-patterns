package com.csen_359.design_patterns.validation;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Chain of Responsibility node - ensures a usage category is present.
 *
 * <p>The category is a typed enum, so an unknown string is already rejected
 * during request binding; this handler guards the remaining null case and is
 * the natural home for any future category-specific rules.
 */
public class CategoryValidationHandler extends UsageEntryHandler {

    @Override
    protected void validate(UsageEntry entry) {
        if (entry.getCategory() == null) {
            throw new ValidationException("category is required");
        }
        // TODO: add category-specific rules here, e.g. DRINKING entries
        //       should never exceed a small per-entry litre cap.
    }
}
