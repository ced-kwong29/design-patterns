package com.csen_359.design_patterns.visitor;

import com.csen_359.design_patterns.domain.UsageEntry;

/**
 * Visitor pattern - the Visitor interface.
 *
 * <p>Adds operations to {@link UsageEntry} data without modifying the entity
 * class. New computations — carbon footprint, estimated cost, per-fixture
 * benchmarking — are introduced by writing a new Visitor implementation rather
 * than adding methods to the entity or forking the service layer.
 */
public interface UsageVisitor {
    void visit(UsageEntry entry);
}
