package org.heyner.excelutils.excel;

import java.nio.file.Path;

public interface ExcelTransferPort {
    int transfer(Path input, Path output, String sheetIn, String sheetOut);
}
