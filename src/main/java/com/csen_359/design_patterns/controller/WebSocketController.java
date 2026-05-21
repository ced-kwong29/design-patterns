package com.csen_359.design_patterns.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * STOMP message endpoint for the live dashboard.
 *
 * <p>Clients connect to the WebSocket endpoint {@code /ws/dashboard} (see
 * {@code WebSocketConfig}) and subscribe to {@code /topic/*}. Most traffic is
 * server-pushed by {@code WebSocketPushListener}; this controller only handles
 * inbound client messages.
 */
@Controller
public class WebSocketController {

    /** Simple round-trip used by the HTML test client to confirm the link. */
    @MessageMapping("/ping")
    @SendTo("/topic/usage")
    public String ping(String payload) {
        return "pong: " + payload;
    }
}
