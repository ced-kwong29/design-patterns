package com.csen_359.design_patterns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.domain.GoalState;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserId(Long userId);

    List<Goal> findByUserIdAndState(Long userId, GoalState state);

    List<Goal> findByState(GoalState state);

    List<Goal> findByStateNot(GoalState state);
}
