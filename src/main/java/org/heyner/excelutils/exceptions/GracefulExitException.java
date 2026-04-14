package org.heyner.excelutils.exceptions;

public class GracefulExitException extends BaseApplicationException {
    public GracefulExitException(String message, int exitCode) {
        super(message, exitCode);
    }
}
