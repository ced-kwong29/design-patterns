package com.csen_359.design_patterns.service.composite;

import com.csen_359.design_patterns.domain.UsageEntry;

/** Composite pattern - Leaf: wraps a single {@link UsageEntry}. */
public class IndividualUsage implements UsageNode {

    private final UsageEntry entry;

    public IndividualUsage(UsageEntry entry) {
        this.entry = entry;
    }

    @Override
    public String name() {
        return entry.getCategory().name() + "#" + entry.getId();
    }

    @Override
    public double totalLitres() {
        return entry.getLitres();
    }
}
