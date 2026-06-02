package org.heyner.excelutils.shared.exceptions;

import lombok.Getter;

@Getter
public class InvalidArgumentCountException extends FunctionalException {

    private final int expected;
    private final int actual;
    private final String commandName;

    public InvalidArgumentCountException(String commandName, int expected, int actual, int exitCode) {
        super("ERROR: invalid number of arguments", exitCode);
        this.commandName = commandName;
        this.expected = expected;
        this.actual = actual;
    }
}