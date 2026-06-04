package com.csen_359.design_patterns.facade;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.report.Report;
import java.util.List;

/**
 * Facade pattern - the aggregated snapshot returned to dashboard clients.
 *
 * <p>Bundles data from four subsystems into one response so the frontend
 * makes a single HTTP call instead of four.
 */
public record DashboardView(
        UsageSummaryResponse usageSummary,
        List<Goal> activeGoals,
        List<Alert> recentAlerts,
        Report latestReport
) {}
