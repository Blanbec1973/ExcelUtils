package org.heyner.excelutils.exceptions;

public class MissingConfigurationException extends FunctionalException {
    public MissingConfigurationException(String msg, int exitCode) {
        super(msg, exitCode);
    }
}
