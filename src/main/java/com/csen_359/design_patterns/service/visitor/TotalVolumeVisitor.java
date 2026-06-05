package com.csen_359.design_patterns.service.visitor;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Visitor pattern - Concrete Visitor: accumulates the raw total litres across
 * all visited entries.
 */
public class TotalVolumeVisitor implements UsageVisitor {

    private double totalLitres = 0.0;

    @Override
    public void visit(UsageEntry entry) {
        totalLitres += entry.getLitres();
    }

    public double getTotalLitres() {
        return totalLitres;
    }
}
