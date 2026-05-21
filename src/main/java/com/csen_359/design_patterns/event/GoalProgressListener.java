package com.csen_359.design_patterns.event;

import com.csen_359.design_patterns.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Observer - reacts to a {@link UsageLoggedEvent} by re-running the goal FSM
 * for any goals the new usage affects.
 */
@Component
public class GoalProgressListener {

    private static final Logger log = LoggerFactory.getLogger(GoalProgressListener.class);

    private final GoalService goalService;

    public GoalProgressListener(GoalService goalService) {
        this.goalService = goalService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUsageLogged(UsageLoggedEvent event) {
        log.info("[Observer] GoalProgressListener handling {}", event);
        goalService.applyUsage(event);
    }
}
