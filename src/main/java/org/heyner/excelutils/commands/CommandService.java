package org.heyner.excelutils.commands;

import java.io.IOException;

public interface CommandService<T extends CommandArgs> {
    String getCommandName();
    void execute(T args) throws IOException;
}
