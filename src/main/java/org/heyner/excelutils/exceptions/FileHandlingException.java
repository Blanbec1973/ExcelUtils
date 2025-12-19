package org.heyner.excelutils.exceptions;

public class FileHandlingException extends FunctionalException {
    public FileHandlingException(String handlerName, Throwable cause, int exitCode) {
        super("Error in handler: " + handlerName + " - " + cause.getMessage(), cause, exitCode);
    }
}

