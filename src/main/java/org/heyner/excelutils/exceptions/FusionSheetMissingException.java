package org.heyner.excelutils.exceptions;

public class FusionSheetMissingException extends FunctionalException {
    public FusionSheetMissingException(String msg, int i) {
        super("No sheet1 in "+ msg,i);
    }
}
