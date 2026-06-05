package com.csen_359.design_patterns.service.command;

/**
 * Command pattern - the Command interface.
 *
 * <p>Encapsulates a user action (log, annotate, delete) as an object so it can
 * be queued, logged, and undone. The {@link UsageCommandInvoker} executes and
 * tracks commands; concrete commands know how to reverse themselves.
 */
public interface UsageCommand {
    void execute();
    void undo();
    String description();
}
