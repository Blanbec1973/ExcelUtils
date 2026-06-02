package org.heyner.excelutils.cli;

public record CommandHelpEntry(
        String name,
        String description,
        String usage,
        String example
) {}
