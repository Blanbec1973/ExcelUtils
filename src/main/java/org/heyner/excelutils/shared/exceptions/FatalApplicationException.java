package org.heyner.excelutils.shared.exceptions;

public class FatalApplicationException extends ApplicationException {
    public FatalApplicationException(String message, int exitCode) {
        super(message, exitCode);
    }
}
