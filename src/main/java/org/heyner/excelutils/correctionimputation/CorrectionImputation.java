package org.heyner.excelutils.correctionimputation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.CommandArgs;
import org.heyner.excelutils.CommandService;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Slf4j
@Service
@RequiredArgsConstructor
public class CorrectionImputation implements CommandService<CorrectionImputationArgs> {
    private final CorrectionImputationConfig correctionImputationConfig;
    private final CorrectionImputationService service;

    private static final String CORRECTION_DISABLED_LOG = "CorrectionImputation is disabled by configuration. Skipping execution.";
    private static final String BEGINNING_CORRECTION_LOG = "Beginning Timesheet correction, file to proceed: {}";
    private static final String PROCESSING_ROW_LOG = "rowNum {} value: {}";
    private static final String LINE_LOG = "Line *{}*";
    private static final String INSERTED_FORMULA_LOG = "Inserted formula {}";
    private static final String ERROR_PROCESSING_FILE_LOG = "Error processing file: {}";

    public void execute(CorrectionImputationArgs args) throws IOException {
        if (!correctionImputationConfig.isCorrectionImputationActionEnabled()) {
            log.info(CORRECTION_DISABLED_LOG);
            return;
        }

        try (ExcelFile fichierExcel = ExcelFile.open(args.inputFile().toString())){
            log.info(BEGINNING_CORRECTION_LOG, args.inputFile());

            int rowNum = 0;
            Sheet dataSheet = fichierExcel.getWorkBook().getSheet(args.sheetName());
            fichierExcel.getWorkBook().setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            //Get iterator to all the rows in current sheet Iterator<Row> rowIterator = dataSheet.iterator()
            for (Row row : dataSheet) {
               Cell cell = row.getCell(56);
               if (cell != null && cell.getCellType() == CellType.STRING) {
                   log.info(PROCESSING_ROW_LOG, rowNum, cell.getStringCellValue());
                   service.processRow(row, fichierExcel);
                }

                rowNum = rowNum + 1;
            }
            fichierExcel.writeFichierExcel();
        } catch (IOException e) {
            log.error(ERROR_PROCESSING_FILE_LOG, args.inputFile(), e);
            throw e;
        }
    }

    @Override
    public String getCommandName() {
        return "correctionimputation";
    }
}
