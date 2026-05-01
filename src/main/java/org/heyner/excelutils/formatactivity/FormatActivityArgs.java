package org.heyner.excelutils.formatactivity;

import lombok.Builder;
import org.heyner.excelutils.CommandArgs;

import java.nio.file.Path;

@Builder
public record FormatActivityArgs(
        Path inputFile
) implements CommandArgs {}
