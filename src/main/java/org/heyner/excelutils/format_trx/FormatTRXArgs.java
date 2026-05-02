package org.heyner.excelutils.format_trx;

import org.heyner.excelutils.commands.CommandArgs;

import java.nio.file.Path;

public record FormatTRXArgs(
        Path inputFile
) implements CommandArgs {}
