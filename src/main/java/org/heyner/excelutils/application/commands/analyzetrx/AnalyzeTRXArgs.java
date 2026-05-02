package org.heyner.excelutils.application.commands.analyzetrx;

import org.heyner.excelutils.application.commands.core.CommandArgs;

import java.nio.file.Path;

public record AnalyzeTRXArgs (
        Path inputFile
) implements CommandArgs  {}
