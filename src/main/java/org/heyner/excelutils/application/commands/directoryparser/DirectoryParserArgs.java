package org.heyner.excelutils.application.commands.directoryparser;

import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

public record DirectoryParserArgs(
        Path directory
) implements CommandArgs {}
