package org.heyner.excelutils.exceptions;

public abstract class FunctionalException extends RuntimeException {
    private final int exitCode;

    protected FunctionalException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    protected FunctionalException(String message, Throwable cause, int exitCode) {
        super(message, cause);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
