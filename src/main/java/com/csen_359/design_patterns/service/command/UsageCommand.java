package com.csen_359.design_patterns.service.command;

/**
 * Command pattern - the Command interface.
 */
public interface UsageCommand {
    void execute();
    void undo();
    String description();
}
