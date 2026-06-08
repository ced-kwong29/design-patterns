package com.csen_359.design_patterns.service.command;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.stereotype.Component;

/**
 * Command pattern - the Invoker.
 */
@Component
public class UsageCommandInvoker {

    private final Deque<UsageCommand> history = new ArrayDeque<>();

    public void execute(UsageCommand command) {
        command.execute();
        history.push(command);
    }

    public void undo() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public String lastCommandDescription() {
        return history.isEmpty() ? null : history.peek().description();
    }
}
