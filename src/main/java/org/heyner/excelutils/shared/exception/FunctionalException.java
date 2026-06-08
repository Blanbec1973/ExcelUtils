package org.heyner.excelutils.shared.exception;

import lombok.Getter;

@Getter
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
}
