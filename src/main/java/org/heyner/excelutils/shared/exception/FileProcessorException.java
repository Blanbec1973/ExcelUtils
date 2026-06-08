package org.heyner.excelutils.shared.exception;

public class FileProcessorException extends FunctionalException {
    public FileProcessorException(String processorName, Throwable cause, int exitCode) {
        super("Error in processor : " + processorName + " - " + cause.getMessage(), cause, exitCode);
    }
}

