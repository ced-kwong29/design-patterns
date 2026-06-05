package com.csen_359.design_patterns.service.command;

import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.dto.LogUsageRequest;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.UsageService;

/**
 * Command pattern - Concrete Command: log a water usage entry.
 *
 * <p>{@code execute()} delegates to {@link UsageService#logUsage} and captures
 * the saved entry. {@code undo()} deletes it by primary key, reversing the
 * action completely (including the Observer fan-out this would normally trigger
 * — production code would also publish a compensating event here).
 */
public class LogUsageCommand implements UsageCommand {

    private final UsageService usageService;
    private final UsageEntryRepository usageEntryRepository;
    private final LogUsageRequest request;

    private UsageEntry savedEntry;

    public LogUsageCommand(UsageService usageService,
                           UsageEntryRepository usageEntryRepository,
                           LogUsageRequest request) {
        this.usageService          = usageService;
        this.usageEntryRepository  = usageEntryRepository;
        this.request               = request;
    }

    @Override
    public void execute() {
        savedEntry = usageService.logUsage(request);
    }

    @Override
    public void undo() {
        if (savedEntry != null) {
            usageEntryRepository.deleteById(savedEntry.getId());
            savedEntry = null;
        }
    }

    @Override
    public String description() {
        return "Log " + request.litres() + " L of " + request.category()
                + " for user " + request.userId();
    }
}
