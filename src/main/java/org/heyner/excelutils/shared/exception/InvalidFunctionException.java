package org.heyner.excelutils.shared.exception;

import lombok.Getter;

@Getter
public class InvalidFunctionException extends FunctionalException {
    private final String functionName;
    public InvalidFunctionException(String functionName, int exitCode) {
        super("unknown command", exitCode);
        this.functionName = functionName;
    }
}
