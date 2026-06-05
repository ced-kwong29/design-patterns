package com.csen_359.design_patterns.service.visitor;

import com.csen_359.design_patterns.domain.UsageCategory;
import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Visitor pattern - Concrete Visitor: builds a per-category total map across
 * all visited entries.
 */
public class CategoryBreakdownVisitor implements UsageVisitor {

    private final Map<UsageCategory, Double> totals = new EnumMap<>(UsageCategory.class);

    @Override
    public void visit(UsageEntry entry) {
        totals.merge(entry.getCategory(), entry.getLitres(), Double::sum);
    }

    public Map<UsageCategory, Double> getTotals() {
        return Collections.unmodifiableMap(totals);
    }
}
