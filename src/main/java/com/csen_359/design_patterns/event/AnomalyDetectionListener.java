package com.csen_359.design_patterns.event;

import com.csen_359.design_patterns.service.AnomalyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Observer - reacts to a {@link UsageLoggedEvent} by running anomaly detection
 * for the affected category. Runs after the write transaction commits, off the
 * request thread.
 */
@Component
public class AnomalyDetectionListener {

    private static final Logger log = LoggerFactory.getLogger(AnomalyDetectionListener.class);

    private final AnomalyService anomalyService;

    public AnomalyDetectionListener(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUsageLogged(UsageLoggedEvent event) {
        log.info("[Observer] AnomalyDetectionListener handling {}", event);
        anomalyService.detectAndSave(event.userId(), event.category());
    }
}
