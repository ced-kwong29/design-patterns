package com.csen_359.design_patterns.service;

import com.csen_359.design_patterns.service.proxy.CachedReportGeneratorProxy;
import com.csen_359.design_patterns.service.report.MonthlyReportGenerator;
import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.service.report.ReportProvider;
import com.csen_359.design_patterns.service.report.WeeklyReportGenerator;
import org.springframework.stereotype.Service;

/**
 * Factory Method pattern - resolves a period string to the correct
 * {@link ReportProvider} and delegates report creation to it.
 *
 * <p>Each generator is wrapped in a {@link CachedReportGeneratorProxy} so that
 * repeated requests within the TTL window (30 minutes) return a cached result
 * without re-running the expensive generation pipeline.
 */
@Service
public class ReportService {

    private final CachedReportGeneratorProxy weeklyProxy;
    private final CachedReportGeneratorProxy monthlyProxy;

    public ReportService(WeeklyReportGenerator weeklyReportGenerator,
                         MonthlyReportGenerator monthlyReportGenerator) {
        this.weeklyProxy = new CachedReportGeneratorProxy(weeklyReportGenerator);
        this.monthlyProxy = new CachedReportGeneratorProxy(monthlyReportGenerator);
    }

    /**
     * Factory Method pattern - selects the appropriate {@link ReportProvider}
     * for the given period and delegates report creation to it.
     *
     * <p>The resolved provider is a caching proxy that transparently delegates
     * to the real {@code ReportGenerator} on a cache miss. The underlying
     * generator uses the Template Method pattern: its {@code final generate()}
     * method defines a fixed algorithm skeleton (gather data → compute totals →
     * break down by category → format), while each subclass overrides only the
     * primitive operations that differ (e.g. the date window).
     */
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
