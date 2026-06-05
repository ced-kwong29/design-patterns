package com.csen_359.design_patterns.service.proxy;

import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.service.report.ReportProvider;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Proxy pattern - a caching proxy for an expensive {@link ReportProvider}.
 *
 * <p>Wraps any {@code ReportProvider} and returns the cached result for
 * {@code TTL} after the first call for a given user. The real generator is only
 * invoked on a cache miss. Callers use the identical {@link ReportProvider}
 * interface and are unaware caching is happening — the proxy is transparent.
 *
 * <p>One proxy instance should be created per user and report type to keep
 * cached results correctly scoped.
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
