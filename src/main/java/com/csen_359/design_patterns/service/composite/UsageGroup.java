package com.csen_359.design_patterns.service.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite pattern - Composite: a named group of UsageNodes.
 *
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
