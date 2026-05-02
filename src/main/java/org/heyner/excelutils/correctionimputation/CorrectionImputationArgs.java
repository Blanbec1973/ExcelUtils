package org.heyner.excelutils.correctionimputation;

import lombok.Builder;
import org.heyner.excelutils.commands.CommandArgs;

import java.nio.file.Path;

@Builder
public record CorrectionImputationArgs(
        Path inputFile,
        String sheetName
) implements CommandArgs {}
