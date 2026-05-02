package org.heyner.excelutils.application.commands.format_trx;

import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

public record FormatTRXArgs(
        Path inputFile
) implements CommandArgs {}
