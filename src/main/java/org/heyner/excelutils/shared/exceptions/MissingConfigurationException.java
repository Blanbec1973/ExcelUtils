package org.heyner.excelutils.shared.exceptions;

public class MissingConfigurationException extends FunctionalException {
    public MissingConfigurationException(int exitCode) {
        super("ERROR: Missing command", exitCode);
    }
}
