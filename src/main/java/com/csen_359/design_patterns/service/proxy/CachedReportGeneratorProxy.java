package com.csen_359.design_patterns.service.proxy;

import java.time.Duration;
import java.time.LocalDateTime;

import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.service.report.ReportProvider;

/**
 * Proxy pattern - a caching proxy for an expensive ReportProvider.
 */
public class CachedReportGeneratorProxy implements ReportProvider {

    private static final Duration TTL = Duration.ofMinutes(30);

    private final ReportProvider delegate;
    private Report cachedReport;
    private LocalDateTime cachedAt;

    public CachedReportGeneratorProxy(ReportProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Report generate(Long userId) {
        if (cachedReport == null || isExpired()) {
            cachedReport = delegate.generate(userId);
            cachedAt = LocalDateTime.now();
        }
        return cachedReport;
    }

    public void invalidate() {
        cachedReport = null;
        cachedAt = null;
    }

    private boolean isExpired() {
        return Duration.between(cachedAt, LocalDateTime.now()).compareTo(TTL) > 0;
    }
}
