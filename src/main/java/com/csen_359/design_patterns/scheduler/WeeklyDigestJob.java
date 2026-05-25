package com.csen_359.design_patterns.scheduler;

import com.csen_359.design_patterns.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Sunday-morning job (08:00) - runs the weekly report pipeline and delivers
 * the digest. Cron is configurable via
 * {@code watermonitor.scheduler.weekly-digest-cron}.
 */
@Component
public class WeeklyDigestJob {

    private static final Logger log = LoggerFactory.getLogger(WeeklyDigestJob.class);

    private final ReportService reportService;

    public WeeklyDigestJob(ReportService reportService) {
        this.reportService = reportService;
    }

    @Scheduled(cron = "${watermonitor.scheduler.weekly-digest-cron}")
    public void run() {
        log.info("WeeklyDigestJob starting");
        // TODO Phase 8: for each active user, call reportService.generateReport(userId, "weekly")
        //      and email or log the digest.
    }
}
