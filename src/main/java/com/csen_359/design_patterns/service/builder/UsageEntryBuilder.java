package com.csen_359.design_patterns.service.builder;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.time.LocalDateTime;

/**
 * Builder pattern - fluent construction of {@link UsageEntry}.
 *
 * <p>{@code userId} and {@code category} are mandatory; {@code build()} throws
 * if either is missing. {@code loggedAt} defaults to "now"; {@code notes} and
 * {@code durationMinutes} are optional and default to {@code null}.
 *
 * <pre>{@code
 * UsageEntry entry = UsageEntryBuilder.builder()
 *         .userId(1L)
 *         .category(UsageCategory.SHOWER)
 *         .litres(45.0)
 *         .durationMinutes(8)
 *         .build();
 * }</pre>
 */
public final class UsageEntryBuilder {

    private Long userId;
    private UsageCategory category;
    private double litres;
    private Integer durationMinutes;
    private LocalDateTime loggedAt;
    private String notes;

    private UsageEntryBuilder() {
    }

    public static UsageEntryBuilder builder() {
        return new UsageEntryBuilder();
    }

    public UsageEntryBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UsageEntryBuilder category(UsageCategory category) {
        this.category = category;
        return this;
    }

    public UsageEntryBuilder litres(double litres) {
        this.litres = litres;
        return this;
    }

    public UsageEntryBuilder durationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public UsageEntryBuilder loggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
        return this;
    }

    public UsageEntryBuilder notes(String notes) {
        this.notes = notes;
        return this;
    }

    public UsageEntry build() {
        if (userId == null) {
            throw new IllegalStateException("UsageEntry requires a userId");
        }
        if (category == null) {
            throw new IllegalStateException("UsageEntry requires a category");
        }
        UsageEntry entry = new UsageEntry();
        entry.setUserId(userId);
        entry.setCategory(category);
        entry.setLitres(litres);
        entry.setDurationMinutes(durationMinutes);
        entry.setLoggedAt(loggedAt != null ? loggedAt : LocalDateTime.now());
        entry.setNotes(notes);
        return entry;
    }
}
