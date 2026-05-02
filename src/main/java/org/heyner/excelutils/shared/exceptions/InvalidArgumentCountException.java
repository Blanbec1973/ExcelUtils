package org.heyner.excelutils.shared.exceptions;

public class InvalidArgumentCountException extends FunctionalException {
    public InvalidArgumentCountException(int expected, int actual, int exitCode) {
        super("Invalid number of arguments, expected : " + expected + ", actual : " + actual,exitCode);
    }
}

