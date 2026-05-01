package org.heyner.excelutils.correctionimputation;

import lombok.Builder;
import org.heyner.excelutils.CommandArgs;

import java.nio.file.Path;

@Builder
public record CorrectionImputationArgs(
        Path inputFile,
        String sheetName
) implements CommandArgs {}
