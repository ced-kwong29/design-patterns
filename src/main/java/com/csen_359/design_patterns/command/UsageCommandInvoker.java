package com.csen_359.design_patterns.command;

import java.util.ArrayDeque;
import java.util.Deque;
import org.springframework.stereotype.Component;

/**
 * Command pattern - the Invoker.
 *
 * <p>Executes commands and maintains a history stack for undo. Callers never
 * call {@code execute()} directly on a command — they ask the invoker, which
 * keeps the history transparent to the rest of the application.
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
