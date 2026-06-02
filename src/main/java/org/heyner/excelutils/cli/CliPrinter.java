package org.heyner.excelutils.cli;

public interface CliPrinter {
    void info(String message);
    void warn(String message);
    void error(String message);
    void blankLine();
}