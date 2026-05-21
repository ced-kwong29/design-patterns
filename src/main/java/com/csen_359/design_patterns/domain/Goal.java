package com.csen_359.design_patterns.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A water conservation goal. {@code state} is driven by the FSM in
 * {@code GoalService} (State pattern).
 *
 * <p>A {@code null} {@code category} means the goal covers overall usage.
 * Construct via {@code GoalBuilder} (Builder pattern).
 */
@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private UsageCategory category;

    @Column(name = "target_litres", nullable = false)
    private double targetLitres;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private GoalPeriod period;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private GoalState state = GoalState.ACTIVE;

    @Column(name = "starts_at", nullable = false)
    private LocalDate startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDate endsAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Goal() {
        // required by JPA; prefer GoalBuilder for application code
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

    public double getTargetLitres() {
        return targetLitres;
    }

    public void setTargetLitres(double targetLitres) {
        this.targetLitres = targetLitres;
    }

    public GoalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(GoalPeriod period) {
        this.period = period;
    }

    public GoalState getState() {
        return state;
    }

    public void setState(GoalState state) {
        this.state = state;
    }

    public LocalDate getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDate getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDate endsAt) {
        this.endsAt = endsAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
