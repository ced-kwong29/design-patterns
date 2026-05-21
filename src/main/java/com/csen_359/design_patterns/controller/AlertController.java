package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.domain.Alert;
import com.csen_359.design_patterns.dto.AlertResponse;
import com.csen_359.design_patterns.repository.AlertRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for anomaly alerts - the {@code /api/alerts} surface.
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /** List a user's unacknowledged alerts. */
    @GetMapping
    public List<AlertResponse> list(@RequestParam Long userId) {
        return alertRepository.findByUserIdAndAcknowledgedAtIsNull(userId).stream()
                .map(AlertResponse::from)
                .toList();
    }

    /** Acknowledge an alert (soft-delete via acknowledged_at). */
    @PatchMapping("/{id}/acknowledge")
    public AlertResponse acknowledge(@PathVariable Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No alert with id " + id));
        alert.acknowledge();
        return AlertResponse.from(alertRepository.save(alert));
    }
}
