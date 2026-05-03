package org.heyner.excelutils.shared.exceptions;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final int exitCode;

    protected ApplicationException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    protected ApplicationException(String message, Throwable cause, int exitCode) {
        super(message, cause);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
