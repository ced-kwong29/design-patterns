package com.csen_359.design_patterns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csen_359.design_patterns.domain.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByUserIdAndAcknowledgedAtIsNull(Long userId);

    List<Alert> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
