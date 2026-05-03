package org.heyner.excelutils.application.commands.core;

public interface Command<T extends CommandArgs> {
    String name();
    T parse(String[] args);
    void execute(T args);
}
