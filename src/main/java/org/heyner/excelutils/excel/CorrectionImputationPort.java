package org.heyner.excelutils.excel;

import org.apache.poi.ss.usermodel.Row;
import org.heyner.common.excelfile.ExcelFile;

public interface CorrectionImputationPort {
    void processRow(Row row, ExcelFile fichierExcel);
}
