package com.csen_359.design_patterns.controller;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.dto.BenchmarkResponse;
import com.csen_359.design_patterns.dto.LogUsageRequest;
import com.csen_359.design_patterns.dto.UsageEntryResponse;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.UsageService;
import com.csen_359.design_patterns.service.command.LogUsageCommand;
import com.csen_359.design_patterns.service.command.UsageCommandInvoker;
import com.csen_359.design_patterns.service.iterator.UsagePageIterator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for usage entries - the {@code /api/usage} surface.
 *
 * <p>The {@code POST} endpoint uses the Command pattern: each log operation
 * is executed via the {@link UsageCommandInvoker}, which tracks history and
 * enables undo via {@code DELETE /api/usage/undo}.
 */
@RestController
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;
    private final UsageCommandInvoker commandInvoker;
    private final UsageEntryRepository usageEntryRepository;

    public UsageController(UsageService usageService,
                           UsageCommandInvoker commandInvoker,
                           UsageEntryRepository usageEntryRepository) {
        this.usageService = usageService;
        this.commandInvoker = commandInvoker;
        this.usageEntryRepository = usageEntryRepository;
    }

    /**
     * Log a new usage entry via the Command pattern. The operation is pushed
     * onto the invoker's history stack, enabling undo.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsageEntryResponse log(@Valid @RequestBody LogUsageRequest request) {
        LogUsageCommand command = new LogUsageCommand(
                usageService, usageEntryRepository, request);
        commandInvoker.execute(command);
        return UsageEntryResponse.from(command.getResult());
    }

    /**
     * Command pattern - undo the most recent usage log operation.
     */
    @DeleteMapping("/undo")
    public Map<String, Object> undo() {
        if (!commandInvoker.canUndo()) {
            return Map.of("undone", false, "message", "Nothing to undo");
        }
        String description = commandInvoker.lastCommandDescription();
        commandInvoker.undo();
        return Map.of("undone", true, "description", description);
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

    /**
     * Iterator pattern - paginated usage history. Returns one page at a time
     * using {@link UsagePageIterator} so the full dataset is never loaded into
     * memory.
     */
    @GetMapping("/page")
    public List<UsageEntryResponse> page(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        UsagePageIterator iterator = new UsagePageIterator(
                usageEntryRepository, userId, from, to, pageSize);
        // Advance to the requested page.
        int current = 0;
        List<UsageEntry> page = List.of();
        while (iterator.hasNext() && current <= pageNum) {
            page = iterator.next();
            current++;
        }
        return page.stream().map(UsageEntryResponse::from).toList();
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
