package org.heyner.excelutils.exceptions;

public class FatalApplicationException extends BaseApplicationException {
    public FatalApplicationException(String message, int exitCode) {
        super(message, exitCode);
    }
}
