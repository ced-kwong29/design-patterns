package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.domain.Goal;
import com.csen_359.design_patterns.dto.CreateGoalRequest;
import com.csen_359.design_patterns.dto.GoalResponse;
import com.csen_359.design_patterns.service.GoalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for conservation goals - the {@code /api/goals} surface.
 */
@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /** Create a new conservation goal. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse create(@Valid @RequestBody CreateGoalRequest request) {
        Goal goal = goalService.createGoal(request);
        return GoalResponse.from(goal, goalService.progressPercent(goal));
    }

    /** List a user's goals with current FSM state and progress. */
    @GetMapping
    public List<GoalResponse> list(@RequestParam Long userId) {
        return goalService.listGoals(userId).stream()
                .map(goal -> GoalResponse.from(goal, goalService.progressPercent(goal)))
                .toList();
    }
}
