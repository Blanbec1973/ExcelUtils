package org.heyner.excelutils.shared.exception;

public class MissingConfigurationException extends FunctionalException {
    public MissingConfigurationException(int exitCode) {
        super("ERROR: Missing command", exitCode);
    }
}
