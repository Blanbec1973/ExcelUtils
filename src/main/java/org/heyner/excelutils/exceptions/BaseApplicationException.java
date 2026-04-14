package org.heyner.excelutils.exceptions;

import lombok.Getter;

@Getter
public abstract class BaseApplicationException extends RuntimeException {
    private final int exitCode;

    protected BaseApplicationException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    protected BaseApplicationException(String message, Throwable cause, int exitCode) {
        super(message, cause);
        this.exitCode = exitCode;
    }

}
