package com.csen_359.design_patterns.service;

import com.csen_359.design_patterns.service.report.MonthlyReportGenerator;
import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.service.report.ReportGenerator;
import com.csen_359.design_patterns.service.report.WeeklyReportGenerator;
import org.springframework.stereotype.Service;

/**
 * Factory Method pattern - resolves a period string to the correct
 * {@link ReportGenerator} (Template Method) subclass and delegates
 * report creation to it.
 */
@Service
public class ReportService {

    private final WeeklyReportGenerator weeklyReportGenerator;
    private final MonthlyReportGenerator monthlyReportGenerator;

    public ReportService(WeeklyReportGenerator weeklyReportGenerator,
                         MonthlyReportGenerator monthlyReportGenerator) {
        this.weeklyReportGenerator = weeklyReportGenerator;
        this.monthlyReportGenerator = monthlyReportGenerator;
    }

    /**
     * Factory Method pattern - selects the appropriate {@link ReportGenerator}
     * subclass for the given period and delegates report creation to it.
     *
     * <p>The resolved generator internally uses the Template Method pattern:
     * its {@code final generate()} method defines a fixed algorithm skeleton
     * (gather data → compute totals → break down by category → format), while
     * each subclass overrides only the primitive operations that differ
     * (e.g. the date window).
     */
    public Report generateReport(Long userId, String period) {
        ReportGenerator generator = switch (period.toLowerCase()) {
            case "weekly" -> weeklyReportGenerator;
            case "monthly" -> monthlyReportGenerator;
            default -> throw new IllegalArgumentException("Unknown report period: " + period);
        };
        return generator.generate(userId);
    }
}
