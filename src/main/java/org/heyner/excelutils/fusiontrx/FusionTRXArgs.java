package org.heyner.excelutils.fusiontrx;

import java.nio.file.Path;

public record FusionTRXArgs(
        Path directory,
        Path outputPath
) {}
