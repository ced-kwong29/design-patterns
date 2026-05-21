package com.csen_359.design_patterns.dto;

import com.csen_359.design_patterns.domain.UsageCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Request body for {@code POST /api/usage}. Bean Validation here is the first,
 * coarse gate; the {@code validation} package (Chain of Responsibility) applies
 * the richer domain rules afterwards.
 */
public record LogUsageRequest(
        @NotNull Long userId,
        @NotNull UsageCategory category,
        @Positive double litres,
        @PositiveOrZero Integer durationMinutes,
        @NotNull LocalDateTime loggedAt,
        @Size(max = 500) String notes) {
}
