package org.heyner.excelutils.shared.exceptions;

public class MissingConfigurationException extends FunctionalException {
    public MissingConfigurationException(String msg, int exitCode) {
        super(msg, exitCode);
    }
}
