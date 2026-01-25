package org.heyner.excelutils.analyzetrx;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.exceptions.TransferDataException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TrxDataTransfer {

    public int transfer(String inputFileName, String outputFileName, String sheetIn, String sheetOut) {
        try (ExcelFile excelIn = ExcelFile.open(inputFileName);
             ExcelFile excelOut = ExcelFile.open(outputFileName)) {
            Integer rowCount = excelIn.rowCount(sheetIn, 0);

            // Creating Range A2:BB3000)
            excelIn.setTileRange(new CellRangeAddress(1, rowCount - 1, 0, 53));
            excelOut.setTileRange(new CellRangeAddress(1, rowCount - 1, 0, 53));
            excelIn.copyRange(excelOut, sheetIn, sheetOut);

            // Creating Range A2:BB3000)
            excelIn.setTileRange(new CellRangeAddress(1, rowCount - 1, 56, 56));
            excelOut.setTileRange(new CellRangeAddress(1, rowCount - 1, 56, 56));
            excelIn.copyRange(excelOut, sheetIn, sheetOut);

            for (Row row : excelOut.getWorkBook().getSheet(sheetOut)) {
                excelOut.evaluateFormulaCell(row.getCell(54));
                excelOut.evaluateFormulaCell(row.getCell(55));
            }

            excelOut.writeFichierExcel();
            return rowCount;
        } catch (IOException e) {
            throw new TransferDataException(e.getMessage(),-1);
        }
    }
}
