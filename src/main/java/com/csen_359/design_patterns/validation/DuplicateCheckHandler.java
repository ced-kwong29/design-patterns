package com.csen_359.design_patterns.validation;

import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDateTime;

/**
 * Chain of Responsibility node - flags a near-duplicate entry: same user and
 * category logged within a 10-minute window of an existing entry.
 */
public class DuplicateCheckHandler extends UsageEntryHandler {

    private static final long WINDOW_MINUTES = 10;

    private final UsageEntryRepository repository;

    public DuplicateCheckHandler(UsageEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void validate(UsageEntry entry) {
        LocalDateTime loggedAt = entry.getLoggedAt();
        if (loggedAt == null) {
            return; // RangeValidationHandler ordering aside, nothing to compare
        }
        long nearby = repository.countByUserIdAndCategoryAndLoggedAtBetween(
                entry.getUserId(),
                entry.getCategory(),
                loggedAt.minusMinutes(WINDOW_MINUTES),
                loggedAt.plusMinutes(WINDOW_MINUTES));
        if (nearby > 0) {
            throw new ValidationException(
                    "a near-identical " + entry.getCategory()
                            + " entry already exists within " + WINDOW_MINUTES + " minutes");
        }
    }
}
