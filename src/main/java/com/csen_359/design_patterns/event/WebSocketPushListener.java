package com.csen_359.design_patterns.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Observer - pushes live updates to the dashboard over STOMP/WebSocket.
 *
 * <p>Subscribes to three domain events and fans each one out to its own
 * destination topic. Clients connect at {@code /ws/dashboard}.
 */
@Component
public class WebSocketPushListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketPushListener.class);

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketPushListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUsageLogged(UsageLoggedEvent event) {
        log.debug("[Observer] pushing usage update to /topic/usage");
        messagingTemplate.convertAndSend("/topic/usage", event);
    }

    @EventListener
    public void onAnomalyDetected(AnomalyDetectedEvent event) {
        log.debug("[Observer] pushing alert to /topic/alerts");
        messagingTemplate.convertAndSend("/topic/alerts", event);
    }

    @EventListener
    public void onGoalStatusChanged(GoalStatusChangedEvent event) {
        log.debug("[Observer] pushing goal update to /topic/goals");
        messagingTemplate.convertAndSend("/topic/goals", event);
    }
}
