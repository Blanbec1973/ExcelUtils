package org.heyner.excelutils.infrastructure.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.ports.FormatActivityPort;
import org.heyner.excelutils.infrastructure.config.FormatActivityConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormatActivityAdapter implements FormatActivityPort {
    private final FormatActivityConfig  formatActivityConfig;
    private static final String FORMATTING_ACTIVITY_FILE_LOG = "Formatting activity file : {}";
    private static final String FATAL_ERROR_PROCESSING_LOG = "ERROR: Fatal error while processing {}";
    private static final int HEADER_ROW_INDEX = 0;
    private static final int HEADER_COLUMN_INDEX = 0;
    private static final int DATA_START_ROW_INDEX = 1;
    private static final int MT_HT_COLUMN_INDEX = 27;
    private static final int STYLE_SOURCE_COLUMN_INDEX = 26;
    private static final int AUTO_FILTER_LAST_COLUMN_INDEX = 27;
    @Override
    public void format(Path inputFile) {
        try(ExcelFile fichierExcel = ExcelFile.open(inputFile.toString())) {
            log.info(FORMATTING_ACTIVITY_FILE_LOG, inputFile);

            Sheet dataSheet = fichierExcel.getWorkBook().getSheetAt(0);
            hideUnusedColumns(dataSheet);

            log.debug("Creating 'Mt HT' column");
            createColumnNoTax(fichierExcel, dataSheet);

            log.debug("Applying auto filter on sheet {}", ExcelConstants.DEFAULT_SHEET);
            dataSheet.setAutoFilter(new CellRangeAddress(HEADER_ROW_INDEX,
                    HEADER_ROW_INDEX,HEADER_COLUMN_INDEX,AUTO_FILTER_LAST_COLUMN_INDEX));

            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET,ExcelConstants.AR_HISTORIC_HEADER);
            fichierExcel.writeFichierExcel();
            log.info("Activity file formatted successfully: {}", inputFile);
        } catch (IOException e) {
            log.error(FATAL_ERROR_PROCESSING_LOG, inputFile, e);
            throw new FatalApplicationException(
                    inputFile.toString(),
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR
            );
        }
    }

    private void hideUnusedColumns(Sheet dataSheet) {
        for (int columnIndex : formatActivityConfig.getHideColumnsSet()) {
            dataSheet.setColumnHidden(columnIndex, true);
        }
    }

    private void createColumnNoTax(ExcelFile excelFile, Sheet dataSheet) {
        for (Row row : dataSheet) {
            if (row.getRowNum() == DATA_START_ROW_INDEX) {
                row.createCell(MT_HT_COLUMN_INDEX).setCellValue("Mt HT");
                CellStyle newCellStyle = row.getCell(STYLE_SOURCE_COLUMN_INDEX).getCellStyle();
                row.getCell(MT_HT_COLUMN_INDEX).setCellStyle(newCellStyle);
            }
            if (row.getRowNum() > DATA_START_ROW_INDEX) {
                int rowNum = row.getRowNum()+1;
                String formula = "S" + rowNum+"/1.2" ;
                row.createCell(MT_HT_COLUMN_INDEX).setCellFormula(formula);
                excelFile.evaluateFormulaCell(row.getCell(MT_HT_COLUMN_INDEX));
            }
        }
    }
}
