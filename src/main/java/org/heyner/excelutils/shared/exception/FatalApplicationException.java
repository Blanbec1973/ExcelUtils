package org.heyner.excelutils.shared.exception;

import lombok.Getter;

@Getter
public class FatalApplicationException extends ApplicationException {
    private final String resource;

    public FatalApplicationException(String resource, Throwable t, int exitCode) {
        super(resource, t, exitCode);
        this.resource = resource;
    }
}
