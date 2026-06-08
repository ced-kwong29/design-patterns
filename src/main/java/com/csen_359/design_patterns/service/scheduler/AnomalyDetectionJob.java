package com.csen_359.design_patterns.service.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.csen_359.design_patterns.service.AnomalyService;

/**
 * Nightly job (02:00) - recomputes the 30-day baseline per category and flags
 * sustained elevation.
 */
@Component
public class AnomalyDetectionJob {

    private static final Logger log = LoggerFactory.getLogger(AnomalyDetectionJob.class);

    private final AnomalyService anomalyService;

    public AnomalyDetectionJob(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @Scheduled(cron = "${watermonitor.scheduler.anomaly-detection-cron}")
    public void run() {
        log.info("AnomalyDetectionJob starting");
        anomalyService.runNightlyDetection();
    }
}
