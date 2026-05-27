package com.csen_359.design_patterns.repository;

import com.csen_359.design_patterns.domain.Alert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Alert}.
 */
public interface AlertRepository extends JpaRepository<Alert, Long> {

    /** Unacknowledged alerts (soft-delete: acknowledged_at is null). */
    List<Alert> findByUserIdAndAcknowledgedAtIsNull(Long userId);

    List<Alert> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
