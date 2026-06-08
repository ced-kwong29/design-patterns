package com.csen_359.design_patterns.service.composite;

/**
 * Composite pattern - the Component interface shared by leaf and composite nodes.
 */
public interface UsageNode {
    String name();
    double totalLitres();
}
