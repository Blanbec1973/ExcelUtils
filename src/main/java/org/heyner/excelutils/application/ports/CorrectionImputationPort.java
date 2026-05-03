package org.heyner.excelutils.application.ports;

import org.apache.poi.ss.usermodel.Row;
import org.heyner.common.excelfile.ExcelFile;

import java.nio.file.Path;

public interface CorrectionImputationPort {
    void correct(Path inputFile, String sheetName);
}
