package org.heyner.excelutils.application.commands.lissage;

import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

public record LissageArgs(
        Path inputFile,
        int lissageRow,
        double targetMargin
) implements CommandArgs {}