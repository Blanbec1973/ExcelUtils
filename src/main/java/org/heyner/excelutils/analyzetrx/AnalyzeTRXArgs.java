package org.heyner.excelutils.analyzetrx;

import org.heyner.excelutils.commands.CommandArgs;

import java.nio.file.Path;

public record AnalyzeTRXArgs (
        Path inputFile
) implements CommandArgs  {}
