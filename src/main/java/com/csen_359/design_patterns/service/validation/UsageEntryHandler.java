package com.csen_359.design_patterns.service.validation;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Chain of Responsibility - abstract node in the usage-entry validation
 * pipeline.
 */
public abstract class UsageEntryHandler {

    private UsageEntryHandler next;

    public UsageEntryHandler linkTo(UsageEntryHandler next) {
        this.next = next;
        return next;
    }

    public final void handle(UsageEntry entry) {
        validate(entry);
        if (next != null) {
            next.handle(entry);
        }
    }

    protected abstract void validate(UsageEntry entry);
}
