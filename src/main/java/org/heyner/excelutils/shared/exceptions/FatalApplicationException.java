package org.heyner.excelutils.shared.exceptions;

public class FatalApplicationException extends ApplicationException {
    public FatalApplicationException(String message, Throwable t, int exitCode) {
        super(message, t, exitCode);
    }
}
