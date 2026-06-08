package org.heyner.excelutils.shared.exception;

public class GracefulExitException extends ApplicationException {
    public GracefulExitException(String message, int exitCode) {
        super(message, exitCode);
    }
}
