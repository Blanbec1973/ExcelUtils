package org.heyner.excelutils.application.commands.formatactivity;

import lombok.Builder;
import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

@Builder
public record FormatActivityArgs(
        Path inputFile
) implements CommandArgs {}
