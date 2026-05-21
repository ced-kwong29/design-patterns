package com.csen_359.design_patterns.service;

import com.csen_359.design_patterns.report.MonthlyReportGenerator;
import com.csen_359.design_patterns.report.Report;
import com.csen_359.design_patterns.report.WeeklyReportGenerator;
import org.springframework.stereotype.Service;

/**
 * Orchestrates report generation by selecting the right Template Method
 * subclass for the requested period.
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

    public Report weeklyReport(Long userId) {
        return weeklyReportGenerator.generate(userId);
    }

    public Report monthlyReport(Long userId) {
        return monthlyReportGenerator.generate(userId);
    }
}
