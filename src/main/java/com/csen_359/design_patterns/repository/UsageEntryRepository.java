package com.csen_359.design_patterns.repository;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for {@link UsageEntry}.
 */
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
     * Iterator pattern: lazily streams a date-windowed slice of usage history
     * without materialising it all in memory. Must be consumed inside an open
     * transaction (see {@code UsageService.exportCsv}).
     */
    Stream<UsageEntry> streamByUserIdAndLoggedAtBetweenOrderByLoggedAtAsc(
            Long userId, LocalDateTime from, LocalDateTime to);

    /** Every distinct user that has logged usage - the sweep set for batch jobs. */
    @Query("SELECT DISTINCT u.userId FROM UsageEntry u")
    List<Long> findDistinctUserIds();

    /**
     * Phase 9 - copies entries older than {@code cutoff} into the cold-storage
     * archive table. Pair with {@link #deleteByLoggedAtBefore(LocalDateTime)}.
     */
    @Modifying
    @Query(value = "INSERT INTO usage_entries_archive "
            + "(id, user_id, category, litres, duration_minutes, logged_at, notes, "
            + " adjusted_litres, created_at, archived_at) "
            + "SELECT id, user_id, category, litres, duration_minutes, logged_at, notes, "
            + " adjusted_litres, created_at, now() "
            + "FROM usage_entries WHERE logged_at < :cutoff",
            nativeQuery = true)
    int archiveOlderThan(@Param("cutoff") LocalDateTime cutoff);

    /** Phase 9 - removes entries older than {@code cutoff} once archived. */
    @Modifying
    @Query("DELETE FROM UsageEntry u WHERE u.loggedAt < :cutoff")
    int deleteByLoggedAtBefore(@Param("cutoff") LocalDateTime cutoff);
}
