package com.csen_359.design_patterns.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Monthly job (1st, 03:00) - archives usage entries older than two years to a
 * cold-storage table. Cron is configurable via
 * {@code watermonitor.scheduler.data-cleanup-cron}.
 */
@Component
public class DataCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(DataCleanupJob.class);

    @Scheduled(cron = "${watermonitor.scheduler.data-cleanup-cron}")
    public void run() {
        log.info("DataCleanupJob starting");
        // TODO Phase 9: move usage_entries older than 2 years into the
        //      cold-storage archive table.
    }
}
