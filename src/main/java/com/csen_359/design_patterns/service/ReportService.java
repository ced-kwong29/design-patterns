package com.csen_359.design_patterns.service;

import org.springframework.stereotype.Service;

import com.csen_359.design_patterns.service.proxy.CachedReportGeneratorProxy;
import com.csen_359.design_patterns.service.report.MonthlyReportGenerator;
import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.service.report.ReportProvider;
import com.csen_359.design_patterns.service.report.WeeklyReportGenerator;

@Service
public class ReportService {

    private final CachedReportGeneratorProxy weeklyProxy;
    private final CachedReportGeneratorProxy monthlyProxy;

    public ReportService(WeeklyReportGenerator weeklyReportGenerator,
                         MonthlyReportGenerator monthlyReportGenerator) {
        this.weeklyProxy = new CachedReportGeneratorProxy(weeklyReportGenerator);
        this.monthlyProxy = new CachedReportGeneratorProxy(monthlyReportGenerator);
    }

    public Report generateReport(Long userId, String period) {
        ReportProvider provider = switch (period.toLowerCase()) {
            case "weekly" -> weeklyProxy;
            case "monthly" -> monthlyProxy;
            default -> throw new IllegalArgumentException("Unknown report period: " + period);
        };
        return provider.generate(userId);
    }

    /** Forces the next call for the given period to bypass the cache. */
    public void invalidateCache(String period) {
        switch (period.toLowerCase()) {
            case "weekly" -> weeklyProxy.invalidate();
            case "monthly" -> monthlyProxy.invalidate();
        }
    }
}
