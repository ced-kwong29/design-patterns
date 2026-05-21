package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.dto.BenchmarkResponse;
import com.csen_359.design_patterns.dto.LogUsageRequest;
import com.csen_359.design_patterns.dto.UsageEntryResponse;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.service.UsageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for usage entries - the {@code /api/usage} surface.
 */
@RestController
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    /** Log a new usage entry. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsageEntryResponse log(@Valid @RequestBody LogUsageRequest request) {
        return UsageEntryResponse.from(usageService.logUsage(request));
    }

    /** Query usage history with optional category filter. */
    @GetMapping
    public List<UsageEntryResponse> list(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) UsageCategory category) {
        return usageService.getUsage(userId, from, to, category).stream()
                .map(UsageEntryResponse::from)
                .toList();
    }

    /** Aggregate totals and per-category breakdown for a period. */
    @GetMapping("/summary")
    public UsageSummaryResponse summary(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "week") String period) {
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = "month".equalsIgnoreCase(period)
                ? LocalDate.now().withDayOfMonth(1).atStartOfDay()
                : to.minusDays(7);
        return usageService.summarise(userId, from, to, period);
    }

    /** Compare the user's usage against seeded regional reference data. */
    @GetMapping("/benchmark")
    public BenchmarkResponse benchmark(
            @RequestParam Long userId,
            @RequestParam UsageCategory category,
            @RequestParam(defaultValue = "DEFAULT") String region) {
        return usageService.benchmark(userId, category, region);
    }

    /** Export raw usage data for a date range as CSV (Iterator pattern). */
    @GetMapping("/export")
    public void export(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "csv") String format,
            HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"usage.csv\"");
        try {
            usageService.exportCsv(userId, from, to, response.getWriter());
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to stream usage export", ex);
        }
    }
}
