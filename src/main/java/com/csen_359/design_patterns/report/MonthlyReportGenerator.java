package com.csen_359.design_patterns.report;

import com.csen_359.design_patterns.anomaly.CompositeDetector;
import com.csen_359.design_patterns.repository.UsageEntryRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Template Method concrete subclass - a calendar-month report.
 */
@Component
public class MonthlyReportGenerator extends ReportGenerator {

    public MonthlyReportGenerator(UsageEntryRepository usageEntryRepository,
                                  CompositeDetector compositeDetector) {
        super(usageEntryRepository, compositeDetector);
    }

    @Override
    protected String reportType() {
        return "MONTHLY";
    }

    @Override
    protected LocalDate windowStart() {
        return LocalDate.now().withDayOfMonth(1);
    }

    @Override
    protected LocalDate windowEnd() {
        LocalDate now = LocalDate.now();
        return now.withDayOfMonth(now.lengthOfMonth());
    }
}
