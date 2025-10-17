package org.heyner.excelutils.exceptions;

public class GracefulExitException extends RuntimeException {
    private final int exitCode;

    public GracefulExitException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
