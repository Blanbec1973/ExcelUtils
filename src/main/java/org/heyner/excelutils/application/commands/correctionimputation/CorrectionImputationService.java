package org.heyner.excelutils.application.commands.correctionimputation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.heyner.common.excelfile.ExcelFile;
import org.springframework.stereotype.Component;

@Component
public class CorrectionImputationService {
    //8 : code   476867
    //9 : nom    POMMERET
    public void processRow(Row row, ExcelFile fichierExcel) {
        Cell cell = row.getCell(56);

        if (cell.getStringCellValue().equals("-Difficulté à déterminer-") &&
                row.getCell(8).getStringCellValue().equals("476867")) {

            int rowNum = row.getRowNum() + 1;
            String formula = "AC" + rowNum + "/8";

            cell.setCellFormula(formula);
            fichierExcel.evaluateFormulaCell(cell);
        }
    }
}
