package org.heyner.excelutils.shared.exceptions;

public class FatalApplicationException extends ApplicationException {
    private final String ressource;

    public FatalApplicationException(String ressource, Throwable t, int exitCode) {
        super(ressource, t, exitCode);
        this.ressource = ressource;
    }

    public String getRessource() {
        return ressource;
    }
}
