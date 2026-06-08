package com.csen_359.design_patterns.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;


public interface UsageEntryRepository extends JpaRepository<UsageEntry, Long> {

    List<UsageEntry> findByUserIdAndLoggedAtBetween(
            Long userId, LocalDateTime from, LocalDateTime to);

    List<UsageEntry> findByUserIdAndLoggedAtBetween(
            Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    List<UsageEntry> findByUserIdAndCategoryAndLoggedAtBetween(
            Long userId, UsageCategory category, LocalDateTime from, LocalDateTime to);

    long countByUserIdAndCategoryAndLoggedAtBetween(
            Long userId, UsageCategory category, LocalDateTime from, LocalDateTime to);

    /**
     * Iterator pattern
     */
    Stream<UsageEntry> streamByUserIdAndLoggedAtBetweenOrderByLoggedAtAsc(
            Long userId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT DISTINCT u.userId FROM UsageEntry u")
    List<Long> findDistinctUserIds();
    @Modifying
    @Query(value = "INSERT INTO usage_entries_archive "
            + "(id, user_id, category, litres, duration_minutes, logged_at, notes, "
            + " adjusted_litres, created_at, archived_at) "
            + "SELECT id, user_id, category, litres, duration_minutes, logged_at, notes, "
            + " adjusted_litres, created_at, now() "
            + "FROM usage_entries WHERE logged_at < :cutoff",
            nativeQuery = true)
    int archiveOlderThan(@Param("cutoff") LocalDateTime cutoff);

    @Modifying
    @Query("DELETE FROM UsageEntry u WHERE u.loggedAt < :cutoff")
    int deleteByLoggedAtBefore(@Param("cutoff") LocalDateTime cutoff);
}
