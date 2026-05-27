package com.csen_359.design_patterns.visitor;

import com.csen_359.design_patterns.domain.UsageEntry;
import java.util.List;

/**
 * Visitor pattern - the dispatcher that drives a {@link UsageVisitor} over a
 * collection of entries.
 *
 * <p>Because {@link UsageEntry} is a JPA entity whose only responsibility is
 * persistence mapping, business-logic dispatch lives here rather than in an
 * {@code accept()} method on the entity. Callers choose the visitor; this
 * class applies it entry-by-entry, preserving the Visitor intent of separating
 * algorithms from the object structure they operate on.
 */
public final class UsageStatisticsApplier {

    private UsageStatisticsApplier() {}

    public static void apply(List<UsageEntry> entries, UsageVisitor visitor) {
        for (UsageEntry entry : entries) {
            visitor.visit(entry);
        }
    }
}
