package correctionimputation;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;


public class CorrectionImputation {
    private static final Logger logger = LogManager.getLogger(CorrectionImputation.class);
    private final FichierExcel fichierExcel;

    public static void main(String[] args) throws IOException {
        logger.info("Beginning Timesheet correction");
        // args[0] : parsing file
        // args[1] : sheet

        new CorrectionImputation(args);
    }

    public CorrectionImputation(String[] args) throws IOException {
        fichierExcel = new FichierExcel(args[0]);
        logger.info("File to proceed : {}", args[0]);

        int rowNum = 0;
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(args[1]);
        fichierExcel.getWorkBook().setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        //Get iterator to all the rows in current sheet Iterator<Row> rowIterator = dataSheet.iterator()
        for (Row row : dataSheet) {
           Cell cell = row.getCell(56);
           if (cell != null && cell.getCellType() == CellType.STRING) {
               logger.info("rowNum {} value : {}",rowNum,cell.getStringCellValue());
               processRow(row);
            }

            rowNum = rowNum+1;
        }
        fichierExcel.writeFichierExcel();
    }

    private void processRow(Row row) {
        //8 : code   476867
        //9 : nom    POMMERET
        Cell cell = row.getCell(56);

        logger.info("Line *{}*", cell.getStringCellValue());
        logger.info("Line *{}*",row.getCell(8).getStringCellValue());

            if (cell.getStringCellValue().equals("-Difficulté à déterminer-") &&
                row.getCell(8).getStringCellValue().equals("476867")) {
            int rowNum = row.getRowNum()+1;
            String formula = "AC" + rowNum +"/8" ;
            cell.setCellFormula(formula);
            fichierExcel.evaluateFormulaCell(cell);
            logger.info("Inserted formula {}",formula);
        }





    }


}
