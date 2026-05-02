package org.heyner.excelutils.application.ports;

import org.apache.poi.ss.usermodel.Row;
import org.heyner.common.excelfile.ExcelFile;

public interface CorrectionImputationPort {
    void processRow(Row row, ExcelFile fichierExcel);
}
