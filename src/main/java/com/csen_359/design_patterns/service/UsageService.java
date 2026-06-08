package com.csen_359.design_patterns.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csen_359.design_patterns.domain.RegionalBenchmark;
import com.csen_359.design_patterns.domain.Season;
import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import com.csen_359.design_patterns.dto.BenchmarkResponse;
import com.csen_359.design_patterns.dto.LogUsageRequest;
import com.csen_359.design_patterns.dto.UsageSummaryResponse;
import com.csen_359.design_patterns.event.UsageLoggedEvent;
import com.csen_359.design_patterns.repository.RegionalBenchmarkRepository;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.service.builder.UsageEntryBuilder;
import com.csen_359.design_patterns.service.calculation.BaseUsageCalculator;
import com.csen_359.design_patterns.service.calculation.RegionalBenchmarkDecorator;
import com.csen_359.design_patterns.service.calculation.SeasonalAdjustmentDecorator;
import com.csen_359.design_patterns.service.calculation.UsageCalculator;
import com.csen_359.design_patterns.service.composite.IndividualUsage;
import com.csen_359.design_patterns.service.composite.UsageGroup;
import com.csen_359.design_patterns.service.validation.UsageEntryHandler;
import com.csen_359.design_patterns.service.visitor.CategoryBreakdownVisitor;
import com.csen_359.design_patterns.service.visitor.TotalVolumeVisitor;
import com.csen_359.design_patterns.service.visitor.UsageStatisticsApplier;

@Service
public class UsageService {

    /** Reference per-day litres used to weight scarce vs. water-rich regions. */
    private static final double REFERENCE_LITRES_PER_DAY = 150.0;

    /** Window, in days, used for the benchmark per-day average. */
    private static final int BENCHMARK_DAYS = 30;

    private final UsageEntryRepository usageEntryRepository;
    private final UsageEntryHandler validationChain;
    private final RegionalBenchmarkRepository regionalBenchmarkRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UsageService(UsageEntryRepository usageEntryRepository,
                        UsageEntryHandler usageValidationChain,
                        RegionalBenchmarkRepository regionalBenchmarkRepository,
                        ApplicationEventPublisher eventPublisher) {
        this.usageEntryRepository = usageEntryRepository;
        this.validationChain = usageValidationChain;
        this.regionalBenchmarkRepository = regionalBenchmarkRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UsageEntry logUsage(LogUsageRequest request) {
        UsageEntry entry = UsageEntryBuilder.builder()
                .userId(request.userId())
                .category(request.category())
                .litres(request.litres())
                .durationMinutes(request.durationMinutes())
                .loggedAt(request.loggedAt())
                .notes(request.notes())
                .build();

        validationChain.handle(entry);

        Season season = seasonOf(entry.getLoggedAt().toLocalDate());
        UsageCalculator calculator = new RegionalBenchmarkDecorator(
                new SeasonalAdjustmentDecorator(new BaseUsageCalculator(), season),
                "DEFAULT",
                regionFactor("DEFAULT", entry.getCategory(), season));
        entry.setAdjustedLitres(calculator.calculate(List.of(entry)));

        UsageEntry saved = usageEntryRepository.save(entry);

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

        // Visitor pattern
        TotalVolumeVisitor totalVisitor = new TotalVolumeVisitor();
        CategoryBreakdownVisitor categoryVisitor = new CategoryBreakdownVisitor();
        UsageStatisticsApplier.apply(entries, totalVisitor);
        UsageStatisticsApplier.apply(entries, categoryVisitor);

        // Composite pattern - build a tree grouping entries by category so
        // totalLitres() recurses uniformly over any depth.
        UsageGroup root = new UsageGroup("All Usage");
        Map<UsageCategory, List<UsageEntry>> grouped = entries.stream()
                .collect(Collectors.groupingBy(UsageEntry::getCategory));
        for (Map.Entry<UsageCategory, List<UsageEntry>> e : grouped.entrySet()) {
            UsageGroup categoryGroup = new UsageGroup(e.getKey().name());
            for (UsageEntry entry : e.getValue()) {
                categoryGroup.add(new IndividualUsage(entry));
            }
            root.add(categoryGroup);
        }

        double compositeTotal = root.totalLitres();
        assert Math.abs(compositeTotal - totalVisitor.getTotalLitres()) < 0.001;

        return new UsageSummaryResponse(period, entries.size(),
                totalVisitor.getTotalLitres(), categoryVisitor.getTotals());
    }

    @Transactional(readOnly = true)
    public BenchmarkResponse benchmark(Long userId, UsageCategory category, String regionCode) {
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(BENCHMARK_DAYS);
        List<UsageEntry> entries = usageEntryRepository
                .findByUserIdAndCategoryAndLoggedAtBetween(userId, category, from, to);

        double userPerDay =
                entries.stream().mapToDouble(UsageEntry::getLitres).sum() / BENCHMARK_DAYS;

        Season season = seasonOf(LocalDate.now());
        double regionalPerDay = regionalBenchmarkRepository
                .findByRegionCodeAndCategoryAndSeason(regionCode, category, season)
                .map(RegionalBenchmark::getAvgLitresPerDay)
                .orElse(0.0);

        double percentDifference = regionalPerDay > 0
                ? (userPerDay - regionalPerDay) / regionalPerDay * 100.0
                : 0.0;

        String message;
        if (regionalPerDay <= 0) {
            message = String.format(
                    "No %s benchmark for region %s in %s yet.", category, regionCode, season);
        } else if (percentDifference <= 0) {
            message = String.format(
                    "You use %.0f%% less %s water than the %s average for %s.",
                    Math.abs(percentDifference), category, regionCode, season);
        } else {
            message = String.format(
                    "You use %.0f%% more %s water than the %s average for %s.",
                    percentDifference, category, regionCode, season);
        }

        return new BenchmarkResponse(
                category, userPerDay, regionalPerDay, percentDifference, message);
    }

    /**
     * Iterator pattern - streams a date-windowed slice of usage history and
     * writes it as CSV without ever holding the whole result set in memory.
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

    private static Season seasonOf(LocalDate date) {
        return switch (date.getMonth()) {
            case DECEMBER, JANUARY, FEBRUARY -> Season.WINTER;
            case MARCH, APRIL, MAY -> Season.SPRING;
            case JUNE, JULY, AUGUST -> Season.SUMMER;
            case SEPTEMBER, OCTOBER, NOVEMBER -> Season.AUTUMN;
        };
    }

    private double regionFactor(String regionCode, UsageCategory category, Season season) {
        Optional<RegionalBenchmark> benchmark = regionalBenchmarkRepository
                .findByRegionCodeAndCategoryAndSeason(regionCode, category, season);
        return benchmark
                .map(b -> b.getAvgLitresPerDay() > 0
                        ? REFERENCE_LITRES_PER_DAY / b.getAvgLitresPerDay()
                        : 1.0)
                .orElse(1.0);
    }
}
