package org.heyner.excelutils.directoryparser;

import org.heyner.excelutils.CommandArgs;

import java.nio.file.Path;

public record DirectoryParserArgs(
        Path directory
) implements CommandArgs {}
