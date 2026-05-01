package org.heyner.excelutils.fusiontrx;

import org.heyner.excelutils.CommandArgs;

import java.nio.file.Path;

public record FusionTRXArgs(
        Path directory,
        Path outputPath
) implements CommandArgs {}
