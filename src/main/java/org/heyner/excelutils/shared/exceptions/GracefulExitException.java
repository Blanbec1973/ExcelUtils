package org.heyner.excelutils.shared.exceptions;

public class GracefulExitException extends ApplicationException {
    public GracefulExitException(String message, int exitCode) {
        super(message, exitCode);
    }
}
