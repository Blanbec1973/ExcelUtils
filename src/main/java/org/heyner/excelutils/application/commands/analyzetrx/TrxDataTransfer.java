package org.heyner.excelutils.application.commands.analyzetrx;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.TransferDataException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j
public class TrxDataTransfer {

    public int transfer(Path inputFileName, Path outputFileName, String sheetIn, String sheetOut) {
        log.info("Transferring data from {} to {}", inputFileName, outputFileName);
        log.debug("Source sheet: {}, target sheet: {}", sheetIn, sheetOut);
        try (ExcelFile excelIn = ExcelFile.open(inputFileName.toString());
             ExcelFile excelOut = ExcelFile.open(outputFileName.toString())) {
            Integer rowCount = excelIn.rowCount(sheetIn, 0);
            log.info("Detected {} row(s) in {}", rowCount, sheetIn);

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
            log.info("Data transfer completed: {} -> {}", inputFileName, outputFileName);
            return rowCount;
        } catch (IOException e) {
            throw new TransferDataException(e.getMessage(), e, ExitCodes.FILE_PROCESSING_ERROR);
        }
    }
}
