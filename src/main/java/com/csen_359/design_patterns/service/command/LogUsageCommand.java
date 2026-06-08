package com.csen_359.design_patterns.service.command;

import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.dto.LogUsageRequest;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.UsageService;

/**
 * Command pattern - Concrete Command: log a water usage entry.
 *
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

    public UsageEntry getResult() {
        return savedEntry;
    }

    @Override
    public String description() {
        return "Log " + request.litres() + " L of " + request.category()
                + " for user " + request.userId();
    }
}
