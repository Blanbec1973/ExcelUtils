package org.heyner.excelutils.commands.commandParser;

import org.heyner.excelutils.commands.CommandArgs;

public interface CommandParser {
    CommandArgs parse(String[] args);
}
