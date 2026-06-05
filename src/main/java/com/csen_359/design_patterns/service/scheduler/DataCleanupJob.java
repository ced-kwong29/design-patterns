package com.csen_359.design_patterns.service.scheduler;

import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Monthly job (1st, 03:00) - archives usage entries older than two years to a
 * cold-storage table. Cron is configurable via
 * {@code watermonitor.scheduler.data-cleanup-cron}.
 */
@Component
public class DataCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(DataCleanupJob.class);

    /** Entries older than this are moved to cold storage. */
    private static final int RETENTION_YEARS = 2;

    private final UsageEntryRepository usageEntryRepository;

    public DataCleanupJob(UsageEntryRepository usageEntryRepository) {
        this.usageEntryRepository = usageEntryRepository;
    }

    @Transactional
    @Scheduled(cron = "${watermonitor.scheduler.data-cleanup-cron}")
    public void run() {
        log.info("DataCleanupJob starting");
        LocalDateTime cutoff = LocalDateTime.now().minusYears(RETENTION_YEARS);

        // Copy first, then delete - both inside the same transaction so a
        // failure rolls back without losing any rows.
        int archived = usageEntryRepository.archiveOlderThan(cutoff);
        int deleted = usageEntryRepository.deleteByLoggedAtBefore(cutoff);

        log.info("DataCleanupJob archived {} and removed {} usage entr{} older than {}",
                archived, deleted, deleted == 1 ? "y" : "ies", cutoff);
    }
}
