package com.csen_359.design_patterns.service.facade;

import java.util.List;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.service.report.Report;

/**
 * Facade pattern - the aggregated snapshot returned to dashboard clients.
 */
public record DashboardView(
        UsageSummaryResponse usageSummary,
        List<Goal> activeGoals,
        List<Alert> recentAlerts,
        Report latestReport
) {}
