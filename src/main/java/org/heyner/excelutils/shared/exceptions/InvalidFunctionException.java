package org.heyner.excelutils.shared.exceptions;

public class InvalidFunctionException extends FunctionalException {
    public InvalidFunctionException(String functionName, int exitCode) {
        super("Invalid function : " + functionName, exitCode);
    }
}
