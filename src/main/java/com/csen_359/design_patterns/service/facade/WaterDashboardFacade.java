package com.csen_359.design_patterns.service.facade;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.domain.GoalState;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.repository.AlertRepository;
import com.csen_359.design_patterns.repository.GoalRepository;
import com.csen_359.design_patterns.service.ReportService;
import com.csen_359.design_patterns.service.UsageService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Facade pattern - hides the complexity of coordinating four subsystems behind
 * a single {@link #getDashboard(long)} call.
 *
 * <p>Dashboard controllers depend only on this class. Each subsystem can be
 * refactored independently; callers see no change. Contrast with a controller
 * that directly wires UsageService, GoalRepository, AlertRepository, and
 * ReportService and would need updating for every subsystem rename.
 */
@Service
public class WaterDashboardFacade {

    private final UsageService usageService;
    private final GoalRepository goalRepository;
    private final AlertRepository alertRepository;
    private final ReportService reportService;

    public WaterDashboardFacade(UsageService usageService,
                                GoalRepository goalRepository,
                                AlertRepository alertRepository,
                                ReportService reportService) {
        this.usageService    = usageService;
        this.goalRepository  = goalRepository;
        this.alertRepository = alertRepository;
        this.reportService   = reportService;
    }

    public DashboardView getDashboard(long userId) {
        LocalDateTime monthStart = LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime now = LocalDateTime.now();

        UsageSummaryResponse summary  = usageService.summarise(userId, monthStart, now, "MONTHLY");
        List<Goal>  activeGoals       = goalRepository.findByUserIdAndState(userId, GoalState.ACTIVE);
        List<Alert> recentAlerts      = alertRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        Report      latestReport      = reportService.generateReport(userId, "monthly");

        return new DashboardView(summary, activeGoals, recentAlerts, latestReport);
    }
}
