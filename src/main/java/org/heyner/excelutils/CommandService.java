package org.heyner.excelutils;

import java.io.IOException;

public interface CommandService<T extends CommandArgs> {
    String getCommandName();
    void execute(CommandArgs args) throws IOException;
}
