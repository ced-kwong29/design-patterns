package com.csen_359.design_patterns.service;

import com.csen_359.design_patterns.builder.UsageEntryBuilder;
import com.csen_359.design_patterns.calculation.BaseUsageCalculator;
import com.csen_359.design_patterns.calculation.RegionalBenchmarkDecorator;
import com.csen_359.design_patterns.calculation.SeasonalAdjustmentDecorator;
import com.csen_359.design_patterns.calculation.UsageCalculator;
import com.csen_359.design_patterns.domain.Season;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.dto.BenchmarkResponse;
import com.csen_359.design_patterns.dto.LogUsageRequest;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.event.UsageLoggedEvent;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.validation.UsageEntryHandler;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Core write path for usage entries. {@link #logUsage(LogUsageRequest)} is the
 * spine of the request-flow diagram in the plan and wires together four
 * patterns:
 *
 * <ol>
 *   <li><b>Builder</b> - assembles the {@link UsageEntry}.</li>
 *   <li><b>Chain of Responsibility</b> - the injected validation chain.</li>
 *   <li><b>Decorator</b> - the calculation stack for adjusted litres.</li>
 *   <li><b>Observer</b> - publishes {@link UsageLoggedEvent}.</li>
 * </ol>
 */
@Service
public class UsageService {

    private final UsageEntryRepository usageEntryRepository;
    private final UsageEntryHandler validationChain;
    private final ApplicationEventPublisher eventPublisher;

    public UsageService(UsageEntryRepository usageEntryRepository,
                        UsageEntryHandler usageValidationChain,
                        ApplicationEventPublisher eventPublisher) {
        this.usageEntryRepository = usageEntryRepository;
        this.validationChain = usageValidationChain;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UsageEntry logUsage(LogUsageRequest request) {
        // 1. Builder - construct the entry.
        UsageEntry entry = UsageEntryBuilder.builder()
                .userId(request.userId())
                .category(request.category())
                .litres(request.litres())
                .durationMinutes(request.durationMinutes())
                .loggedAt(request.loggedAt())
                .notes(request.notes())
                .build();

        // 2. Chain of Responsibility - validate / sanitise / reject.
        validationChain.handle(entry);

        // 3. Decorator - stack adjustments to compute adjusted litres.
        UsageCalculator calculator = new RegionalBenchmarkDecorator(
                new SeasonalAdjustmentDecorator(new BaseUsageCalculator(), currentSeason()),
                "DEFAULT");
        entry.setAdjustedLitres(calculator.calculate(List.of(entry)));

        // 4. Persist.
        UsageEntry saved = usageEntryRepository.save(entry);

        // 5. Observer - fan out to anomaly / goal / websocket listeners.
        eventPublisher.publishEvent(new UsageLoggedEvent(
                saved.getId(), saved.getUserId(), saved.getCategory(),
                saved.getLitres(), saved.getLoggedAt()));

        return saved;
    }

    @Transactional(readOnly = true)
    public List<UsageEntry> getUsage(Long userId, LocalDateTime from, LocalDateTime to,
                                     UsageCategory category) {
        if (category != null) {
            return usageEntryRepository
                    .findByUserIdAndCategoryAndLoggedAtBetween(userId, category, from, to);
        }
        return usageEntryRepository.findByUserIdAndLoggedAtBetween(userId, from, to);
    }

    @Transactional(readOnly = true)
    public UsageSummaryResponse summarise(Long userId, LocalDateTime from, LocalDateTime to,
                                          String period) {
        List<UsageEntry> entries =
                usageEntryRepository.findByUserIdAndLoggedAtBetween(userId, from, to);
        double total = entries.stream().mapToDouble(UsageEntry::getLitres).sum();
        Map<UsageCategory, Double> byCategory = entries.stream().collect(Collectors.groupingBy(
                UsageEntry::getCategory, Collectors.summingDouble(UsageEntry::getLitres)));
        return new UsageSummaryResponse(period, entries.size(), total, byCategory);
    }

    @Transactional(readOnly = true)
    public BenchmarkResponse benchmark(Long userId, UsageCategory category, String regionCode) {
        // TODO Phase 6: compute the user's per-day average, look up the regional
        //      benchmark and express the percentile difference.
        throw new UnsupportedOperationException(
                "benchmark() is implemented in Phase 6 - Decorator & Benchmarking");
    }

    /**
     * Iterator pattern - streams a date-windowed slice of usage history and
     * writes it as CSV without ever holding the whole result set in memory.
     * Must run inside a transaction so the underlying cursor stays open.
     */
    @Transactional(readOnly = true)
    public void exportCsv(Long userId, LocalDateTime from, LocalDateTime to, Writer writer) {
        try (Stream<UsageEntry> entries = usageEntryRepository
                .streamByUserIdAndLoggedAtBetweenOrderByLoggedAtAsc(userId, from, to)) {
            writer.append("id,userId,category,litres,durationMinutes,loggedAt,notes\n");
            entries.forEach(e -> appendCsvRow(writer, e));
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to export usage CSV", ex);
        }
    }

    private static void appendCsvRow(Writer writer, UsageEntry e) {
        try {
            writer.append(String.valueOf(e.getId())).append(',')
                    .append(String.valueOf(e.getUserId())).append(',')
                    .append(String.valueOf(e.getCategory())).append(',')
                    .append(String.valueOf(e.getLitres())).append(',')
                    .append(String.valueOf(e.getDurationMinutes())).append(',')
                    .append(String.valueOf(e.getLoggedAt())).append(',')
                    .append(e.getNotes() == null ? "" : e.getNotes().replace(",", " "))
                    .append('\n');
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to write CSV row", ex);
        }
    }

    private static Season currentSeason() {
        // TODO Phase 6: derive the season from the logged-at date / hemisphere.
        return Season.SUMMER;
    }
}
