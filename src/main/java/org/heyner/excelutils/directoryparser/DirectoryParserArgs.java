package org.heyner.excelutils.directoryparser;

import org.heyner.excelutils.commands.CommandArgs;

import java.nio.file.Path;

public record DirectoryParserArgs(
        Path directory
) implements CommandArgs {}
