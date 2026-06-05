package com.csen_359.design_patterns.service.scheduler;

import com.csen_359.design_patterns.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Recalculates the FSM state of every active goal, every 6 hours. Cron is
 * configurable via {@code watermonitor.scheduler.goal-recalc-cron}.
 */
@Component
public class GoalStatusRecalcJob {

    private static final Logger log = LoggerFactory.getLogger(GoalStatusRecalcJob.class);

    private final GoalService goalService;

    public GoalStatusRecalcJob(GoalService goalService) {
        this.goalService = goalService;
    }

    @Scheduled(cron = "${watermonitor.scheduler.goal-recalc-cron}")
    public void run() {
        log.info("GoalStatusRecalcJob starting");
        goalService.recalculateAll();
    }
}
