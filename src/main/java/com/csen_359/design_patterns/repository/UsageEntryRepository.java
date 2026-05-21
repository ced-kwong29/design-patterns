package com.csen_359.design_patterns.repository;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link UsageEntry}.
 */
public interface UsageEntryRepository extends JpaRepository<UsageEntry, Long> {

    List<UsageEntry> findByUserIdAndLoggedAtBetween(
            Long userId, LocalDateTime from, LocalDateTime to);

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
}
