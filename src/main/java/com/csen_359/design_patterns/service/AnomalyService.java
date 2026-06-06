package com.csen_359.design_patterns.service;

import com.csen_359.design_patterns.service.anomaly.CompositeDetector;
import com.csen_359.design_patterns.service.mediator.AlertCoordinator;
import com.csen_359.design_patterns.service.singleton.ConservationThresholds;
import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.event.AnomalyDetectedEvent;
import com.csen_359.design_patterns.repository.AlertRepository;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegates anomaly detection to the {@link CompositeDetector} (Strategy +
 * composite) and persists any resulting alerts, publishing an
 * {@link AnomalyDetectedEvent} per alert (Observer).
 */
@Service
public class AnomalyService {

    private static final Logger log = LoggerFactory.getLogger(AnomalyService.class);

    /** Rolling baseline window used to judge what counts as "normal". */
    private static final int BASELINE_DAYS = 30;

    private final CompositeDetector compositeDetector;
    private final UsageEntryRepository usageEntryRepository;
    private final AlertRepository alertRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AlertCoordinator alertCoordinator;

    public AnomalyService(CompositeDetector compositeDetector,
                          UsageEntryRepository usageEntryRepository,
                          AlertRepository alertRepository,
                          ApplicationEventPublisher eventPublisher,
                          AlertCoordinator alertCoordinator) {
        this.compositeDetector = compositeDetector;
        this.usageEntryRepository = usageEntryRepository;
        this.alertRepository = alertRepository;
        this.eventPublisher = eventPublisher;
        this.alertCoordinator = alertCoordinator;
    }

    /**
     * Runs every detector strategy over the recent window for one category and
     * persists whatever alerts come back. Uses {@link ConservationThresholds}
     * (Singleton) for the critical-alert threshold and notifies the
     * {@link AlertCoordinator} (Mediator) for spike alerts.
     */
    @Transactional
    public List<Alert> detectAndSave(Long userId, UsageCategory category) {
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(BASELINE_DAYS);
        List<UsageEntry> recent = usageEntryRepository
                .findByUserIdAndCategoryAndLoggedAtBetween(userId, category, from, to);

        List<Alert> alerts = compositeDetector.detectAll(recent, category);
        if (alerts.isEmpty()) {
            return List.of();
        }

        ConservationThresholds thresholds = ConservationThresholds.getInstance();
        List<Alert> saved = alertRepository.saveAll(alerts);
        for (Alert alert : saved) {
            eventPublisher.publishEvent(new AnomalyDetectedEvent(
                    alert.getId(), alert.getUserId(), alert.getType(),
                    alert.getCategory(), alert.getMessage()));

            // Mediator pattern - route spike alerts through the coordinator.
            if (alert.getType() == com.csen_359.design_patterns.domain.AlertType.SPIKE) {
                alertCoordinator.onUsageSpike(userId, category,
                        recent.get(recent.size() - 1).getLitres(),
                        thresholds.getSpikeMultiplier());
            } else if (alert.getType() == com.csen_359.design_patterns.domain.AlertType.SUSTAINED_ELEVATION) {
                alertCoordinator.onSustainedElevation(userId, category,
                        thresholds.getSustainedElevationDays());
            }
        }
        log.info("Persisted {} alert(s) for user {} / {}", saved.size(), userId, category);
        return saved;
    }

    /**
     * Entry point for {@code AnomalyDetectionJob} - the nightly sweep across
     * all users and categories.
     */
    @Transactional
    public void runNightlyDetection() {
        List<Long> userIds = usageEntryRepository.findDistinctUserIds();
        int totalAlerts = 0;
        for (Long userId : userIds) {
            for (UsageCategory category : UsageCategory.values()) {
                totalAlerts += detectAndSave(userId, category).size();
            }
        }
        log.info("runNightlyDetection() swept {} user(s) x {} categor(ies), raised {} alert(s)",
                userIds.size(), UsageCategory.values().length, totalAlerts);
    }
}
