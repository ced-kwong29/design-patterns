package com.csen_359.design_patterns.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP-over-WebSocket configuration for the live dashboard.
 *
 * <ul>
 *   <li>Clients connect at {@code /ws/dashboard}.</li>
 *   <li>Server pushes land on {@code /topic/*} (usage, alerts, goals).</li>
 *   <li>Inbound client messages are prefixed {@code /app} (see
 *       {@code WebSocketController}).</li>
 * </ul>
 *
 * Enabling the broker also registers the {@code SimpMessagingTemplate} bean
 * that {@code WebSocketPushListener} depends on.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/dashboard")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
