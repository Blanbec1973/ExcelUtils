package org.heyner.excelutils.analyzetrx;

import org.heyner.excelutils.CommandArgs;

import java.nio.file.Path;

public record AnalyzeTRXArgs (
        Path inputFile
) implements CommandArgs  {}
