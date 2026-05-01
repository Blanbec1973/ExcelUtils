package org.heyner.excelutils.formatinvregisterln;

import lombok.Builder;
import org.heyner.excelutils.CommandArgs;

import java.nio.file.Path;

@Builder
public record FormatInvRegisterLNArgs(
        Path inputFile
) implements CommandArgs {}