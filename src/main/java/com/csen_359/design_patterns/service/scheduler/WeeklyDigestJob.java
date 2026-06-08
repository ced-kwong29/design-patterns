package com.csen_359.design_patterns.service.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.ReportService;
import com.csen_359.design_patterns.service.bridge.DigestNotification;
import com.csen_359.design_patterns.service.bridge.NotificationChannel;
import com.csen_359.design_patterns.service.report.Report;

/**
 * Sunday-morning job (08:00) - runs the weekly report pipeline and delivers
 * the digest via the Bridge notification stack.
 */
@Component
public class WeeklyDigestJob {

    private static final Logger log = LoggerFactory.getLogger(WeeklyDigestJob.class);

    private final ReportService reportService;
    private final UsageEntryRepository usageEntryRepository;
    private final NotificationChannel notificationChannel;

    public WeeklyDigestJob(ReportService reportService,
                           UsageEntryRepository usageEntryRepository,
                           NotificationChannel notificationChannel) {
        this.reportService = reportService;
        this.usageEntryRepository = usageEntryRepository;
        this.notificationChannel = notificationChannel;
    }

    @Scheduled(cron = "${watermonitor.scheduler.weekly-digest-cron}")
    public void run() {
        log.info("WeeklyDigestJob starting");
        List<Long> userIds = usageEntryRepository.findDistinctUserIds();
        for (Long userId : userIds) {
            Report report = reportService.generateReport(userId, "weekly");

            // Bridge pattern - compose digest items and dispatch via the channel.
            List<String> items = new ArrayList<>();
            items.add(String.format("Total usage: %.1f L", report.totalLitres()));
            for (Map.Entry<UsageCategory, Double> e : report.litresByCategory().entrySet()) {
                items.add(String.format("%s: %.1f L", e.getKey(), e.getValue()));
            }
            if (report.anomalyCount() > 0) {
                items.add(String.format("Anomalies detected: %d", report.anomalyCount()));
            }

            DigestNotification digest = new DigestNotification(
                    notificationChannel, "Weekly", items);
            digest.dispatch(userId);

            log.info("Weekly digest for user {}: {}", userId, report.summary());
        }
        log.info("WeeklyDigestJob delivered {} digest(s)", userIds.size());
    }
}
