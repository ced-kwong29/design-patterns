package com.csen_359.design_patterns.report;

import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Template Method concrete subclass - a rolling 7-day report.
 */
@Component
public class WeeklyReportGenerator extends ReportGenerator {

    public WeeklyReportGenerator(UsageEntryRepository usageEntryRepository) {
        super(usageEntryRepository);
    }

    @Override
    protected String reportType() {
        return "WEEKLY";
    }

    @Override
    protected LocalDate windowStart() {
        return LocalDate.now().minusDays(6);
    }

    @Override
    protected LocalDate windowEnd() {
        return LocalDate.now();
    }
}
