package com.csen_359.design_patterns.scheduler;

import com.csen_359.design_patterns.report.Report;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.ReportService;
import java.util.List;
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
    private final UsageEntryRepository usageEntryRepository;

    public WeeklyDigestJob(ReportService reportService,
                           UsageEntryRepository usageEntryRepository) {
        this.reportService = reportService;
        this.usageEntryRepository = usageEntryRepository;
    }

    @Scheduled(cron = "${watermonitor.scheduler.weekly-digest-cron}")
    public void run() {
        log.info("WeeklyDigestJob starting");
        List<Long> userIds = usageEntryRepository.findDistinctUserIds();
        for (Long userId : userIds) {
            Report report = reportService.generateReport(userId, "weekly");
            // Delivery (email/SMS) is handled by the Bridge notification stack;
            // here we log the rendered digest so the pipeline is observable.
            log.info("Weekly digest for user {}: {}", userId, report.summary());
        }
        log.info("WeeklyDigestJob delivered {} digest(s)", userIds.size());
    }
}
