package org.heyner.excelutils.shared.constants;

public final class ExitCodes {

    public static final int SUCCESS = 0;

    public static final int UNEXPECTED_ERROR = 1;

    public static final int USAGE_ERROR = 2;

    public static final int CONFIG_ERROR = 3;

    public static final int FILE_PROCESSING_ERROR = 4;

    private ExitCodes() {
        throw new UnsupportedOperationException("Utility class.");
    }
}
