package org.heyner.excelutils.application.commands.core.commandParser;

import org.heyner.excelutils.application.commands.core.CommandArgs;

public interface CommandParser {
    CommandArgs parse(String[] args);
}
