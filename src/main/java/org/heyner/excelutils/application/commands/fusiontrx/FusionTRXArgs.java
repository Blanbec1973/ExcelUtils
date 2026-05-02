package org.heyner.excelutils.application.commands.fusiontrx;

import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

public record FusionTRXArgs(
        Path directory,
        Path outputPath
) implements CommandArgs {}
