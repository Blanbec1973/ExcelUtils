package org.heyner.excelutils;

public class FatalApplicationException extends RuntimeException {
    private final int exitCode;

    public FatalApplicationException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
