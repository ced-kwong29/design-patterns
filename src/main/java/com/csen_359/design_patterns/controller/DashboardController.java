package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.service.facade.DashboardView;
import com.csen_359.design_patterns.service.facade.WaterDashboardFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoint for the aggregated dashboard view. Uses the Facade pattern:
 * a single call to {@link WaterDashboardFacade#getDashboard(long)} replaces
 * four independent subsystem calls.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final WaterDashboardFacade facade;

    public DashboardController(WaterDashboardFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public DashboardView dashboard(@RequestParam Long userId) {
        return facade.getDashboard(userId);
    }
}
