package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.report.Report;
import com.csen_359.design_patterns.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
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

    /** Latest rolling weekly report. */
    @GetMapping("/weekly")
    public Report weekly(@RequestParam Long userId) {
        return reportService.weeklyReport(userId);
    }

    /** Latest calendar-month report. */
    @GetMapping("/monthly")
    public Report monthly(@RequestParam Long userId) {
        return reportService.monthlyReport(userId);
    }
}
