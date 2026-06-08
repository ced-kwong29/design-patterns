package com.csen_359.design_patterns.service.report;

/**
 * Proxy pattern - the Subject interface shared by the real generator and its
 * caching proxy.
 */
public interface ReportProvider {
    Report generate(Long userId);
}
