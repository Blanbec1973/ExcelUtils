package org.heyner.excelutils.commands.commandParser;

import org.heyner.excelutils.CommandArgs;

public interface CommandParser {
    CommandArgs parse(String[] args);
}
