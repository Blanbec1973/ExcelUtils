package org.heyner.excelutils.shared.exceptions;

public class FileProcessorException extends FunctionalException {
    public FileProcessorException(String processorName, Throwable cause, int exitCode) {
        super("Error in processor : " + processorName + " - " + cause.getMessage(), cause, exitCode);
    }
}

