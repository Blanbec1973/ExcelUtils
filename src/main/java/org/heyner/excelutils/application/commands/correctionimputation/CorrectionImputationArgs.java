package org.heyner.excelutils.application.commands.correctionimputation;

import lombok.Builder;
import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

@Builder
public record CorrectionImputationArgs(
        Path inputFile,
        String sheetName
) implements CommandArgs {}
