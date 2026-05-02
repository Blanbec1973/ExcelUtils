package org.heyner.excelutils.application.commands.formatinvregisterln;

import lombok.Builder;
import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

@Builder
public record FormatInvRegisterLNArgs(
        Path inputFile
) implements CommandArgs {}