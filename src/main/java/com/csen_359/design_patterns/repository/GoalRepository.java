package com.csen_359.design_patterns.repository;

import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.domain.GoalState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Goal}.
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserId(Long userId);

    List<Goal> findByState(GoalState state);

    List<Goal> findByStateNot(GoalState state);
}
