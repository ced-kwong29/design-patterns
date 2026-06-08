package com.csen_359.design_patterns.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * A single logged water usage entry.
 *
 */
@Entity
@Table(name = "usage_entries")
public class UsageEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UsageCategory category;

    @Column(nullable = false)
    private double litres;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    @Column(length = 500)
    private String notes;

    /** Set by the calculation package (Decorator). */
    @Column(name = "adjusted_litres")
    private Double adjustedLitres;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UsageEntry() {
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UsageCategory getCategory() {
        return category;
    }

    public void setCategory(UsageCategory category) {
        this.category = category;
    }

    public double getLitres() {
        return litres;
    }

    public void setLitres(double litres) {
        this.litres = litres;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getAdjustedLitres() {
        return adjustedLitres;
    }

    public void setAdjustedLitres(Double adjustedLitres) {
        this.adjustedLitres = adjustedLitres;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
