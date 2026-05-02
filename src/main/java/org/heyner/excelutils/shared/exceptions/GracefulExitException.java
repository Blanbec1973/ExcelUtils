package org.heyner.excelutils.shared.exceptions;

public class GracefulExitException extends BaseApplicationException {
    public GracefulExitException(String message, int exitCode) {
        super(message, exitCode);
    }
}
