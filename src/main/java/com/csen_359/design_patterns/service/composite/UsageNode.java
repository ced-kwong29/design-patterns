package com.csen_359.design_patterns.service.composite;

/**
 * Composite pattern - the Component interface shared by leaf and composite nodes.
 *
 * <p>Callers work with any {@code UsageNode} — a single entry or a named group —
 * and call {@code totalLitres()} without knowing the node's depth in the tree.
 * This lets the dashboard render "Indoor: 48 L" and "All Household: 87 L" with
 * identical code.
 */
public interface UsageNode {
    String name();
    double totalLitres();
}
