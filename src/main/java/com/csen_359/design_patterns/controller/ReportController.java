package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.service.report.Report;
import com.csen_359.design_patterns.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for generated reports - the {@code /api/reports} surface.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Generate a report for the given period.
     * Example: GET /api/reports/weekly?userId=1
     *          GET /api/reports/monthly?userId=1
     */
    @GetMapping("/{period}")
    public Report report(@PathVariable String period, @RequestParam Long userId) {
        return reportService.generateReport(userId, period);
    }
}
