package com.csen_359.design_patterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite pattern - Composite: a named group of {@link UsageNode}s.
 *
 * <p>Groups can be nested arbitrarily — e.g., "Indoor" contains shower, bath,
 * laundry, and dishwasher leaves; "All Household" contains "Indoor" and
 * "Outdoor" groups. {@code totalLitres()} recurses to any depth, so callers
 * never need to know the tree structure.
 */
public class UsageGroup implements UsageNode {

    private final String name;
    private final List<UsageNode> children = new ArrayList<>();

    public UsageGroup(String name) {
        this.name = name;
    }

    public UsageGroup add(UsageNode node) {
        children.add(node);
        return this;
    }

    public UsageGroup remove(UsageNode node) {
        children.remove(node);
        return this;
    }

    public List<UsageNode> children() {
        return List.copyOf(children);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public double totalLitres() {
        return children.stream().mapToDouble(UsageNode::totalLitres).sum();
    }
}
