package org.heyner.excelutils.application.commands.core;

import java.io.IOException;

public interface CommandService<T extends CommandArgs> {
    String getCommandName();
    void execute(T args) throws IOException;
}
