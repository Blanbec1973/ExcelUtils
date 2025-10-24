package org.heyner.excelutils.correctionimputation;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Slf4j
@Service
public class CorrectionImputation implements CommandService {
    private ExcelFile fichierExcel;
    private final CorrectionImputationConfig correctionImputationConfig;

    @Autowired
    public CorrectionImputation(CorrectionImputationConfig correctionImputationConfig) {
        this.correctionImputationConfig = correctionImputationConfig;
    }

    public void execute(String... args) throws IOException {

        if (!correctionImputationConfig.isCorrectionImputationActionEnabled()) {
            log.info("CorrectionImputation is disabled by configuration. Skipping execution.");
            return;
        }

        log.info("Beginning Timesheet correction, file to proceed : {}", args[0]);
        fichierExcel = ExcelFile.open(args[0]);

        int rowNum = 0;
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(args[1]);
        fichierExcel.getWorkBook().setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        //Get iterator to all the rows in current sheet Iterator<Row> rowIterator = dataSheet.iterator()
        for (Row row : dataSheet) {
           Cell cell = row.getCell(56);
           if (cell != null && cell.getCellType() == CellType.STRING) {
               log.info("rowNum {} value : {}",rowNum,cell.getStringCellValue());
               processRow(row);
            }

            rowNum = rowNum+1;
        }
        fichierExcel.writeFichierExcel();
    }

    void processRow(Row row) {
        //8 : code   476867
        //9 : nom    POMMERET
        Cell cell = row.getCell(56);

        log.info("Line *{}*", cell.getStringCellValue());
        log.info("Line *{}*",row.getCell(8).getStringCellValue());

            if (cell.getStringCellValue().equals("-Difficulté à déterminer-") &&
                row.getCell(8).getStringCellValue().equals("476867")) {
            int rowNum = row.getRowNum()+1;
            String formula = "AC" + rowNum +"/8" ;
            cell.setCellFormula(formula);
            fichierExcel.evaluateFormulaCell(cell);
            log.info("Inserted formula {}",formula);
        }
    }
    @Override
    public String getCommandName() {
        return "correctionimputation";
    }
}
