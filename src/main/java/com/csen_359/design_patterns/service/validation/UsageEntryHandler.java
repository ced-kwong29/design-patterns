package com.csen_359.design_patterns.service.validation;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Chain of Responsibility - abstract node in the usage-entry validation
 * pipeline.
 *
 * <p>Each concrete handler implements {@link #validate(UsageEntry)} and either
 * returns quietly (pass) or throws {@link ValidationException} (reject). The
 * chain is assembled in {@code ValidationChainConfig}; handlers never know who
 * comes before or after them.
 */
public abstract class UsageEntryHandler {

    private UsageEntryHandler next;

    /**
     * Links {@code next} after this handler.
     *
     * @return the handler just linked, so calls can be fluently chained:
     *         {@code a.linkTo(b).linkTo(c)}
     */
    public UsageEntryHandler linkTo(UsageEntryHandler next) {
        this.next = next;
        return next;
    }

    /** Runs this handler, then forwards to the rest of the chain. */
    public final void handle(UsageEntry entry) {
        validate(entry);
        if (next != null) {
            next.handle(entry);
        }
    }

    /**
     * Validates one entry. Implementations throw {@link ValidationException}
     * to reject; returning normally passes control down the chain.
     */
    protected abstract void validate(UsageEntry entry);
}
