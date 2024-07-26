package formatactivity;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;

public class FormatActivity {
    private static final Logger logger = LogManager.getLogger(FormatActivity.class);
    private final FichierExcel fichierExcel;

    public FormatActivity(String[] args) throws IOException {
        fichierExcel = new FichierExcel(args[0]);
        logger.info("File to process : {}", args[0]);

        Sheet dataSheet = fichierExcel.getWorkBook().getSheetAt(0);
        hideUnusefulColumns(dataSheet);

        createColumnNoTax(dataSheet);

        dataSheet.setAutoFilter(new CellRangeAddress(1,1,0,27));

        fichierExcel.deleteFirstLineContaining("sheet1","AR Historic by client");
        fichierExcel.writeFichierExcel();
    }

    private void hideUnusefulColumns(Sheet dataSheet) {
        dataSheet.getRow(0).setZeroHeight(true);

        dataSheet.setColumnHidden(0, true);
        dataSheet.setColumnHidden(1, true);
        dataSheet.setColumnHidden(2, true);
        dataSheet.setColumnHidden(3, true);
        dataSheet.setColumnHidden(4, true);
        dataSheet.setColumnHidden(5, true);
        dataSheet.setColumnHidden(8, true);
        dataSheet.setColumnHidden(9, true);
        dataSheet.setColumnHidden(12, true);
        dataSheet.setColumnHidden(15, true);
        dataSheet.setColumnHidden(16, true);
        dataSheet.setColumnHidden(17, true);
        dataSheet.setColumnHidden(20, true);
        dataSheet.setColumnHidden(21, true);
        dataSheet.setColumnHidden(22, true);
        dataSheet.setColumnHidden(23, true);
        dataSheet.setColumnHidden(24, true);
        dataSheet.setColumnHidden(25, true);
        dataSheet.setColumnHidden(26, true);
    }

    private void createColumnNoTax(Sheet dataSheet) {
        for (Row row : dataSheet) {
            if (row.getRowNum() == 1) {
                row.createCell(27).setCellValue("Mt HT");
                CellStyle newCellStyle = row.getCell(26).getCellStyle();
                row.getCell(27).setCellStyle(newCellStyle);

            }
            if (row.getRowNum() > 1) {
                int rowNum = row.getRowNum()+1;
                String formula = "S" + rowNum+"/1.2" ;
                row.createCell(27).setCellFormula(formula);
                fichierExcel.evaluateFormulaCell(row.getCell(27));
            }

        }
    }

}
