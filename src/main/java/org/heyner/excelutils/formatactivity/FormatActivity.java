package org.heyner.excelutils.formatactivity;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Slf4j
@Service
public class FormatActivity implements CommandService {
    public void execute(String... args) throws IOException {
        try(ExcelFile fichierExcel = new ExcelFile(args[0])) {
            log.info("File to process : {}", args[0]);

            Sheet dataSheet = fichierExcel.getWorkBook().getSheetAt(0);
            hideUnusefulColumns(dataSheet);

            createColumnNoTax(fichierExcel, dataSheet);

            dataSheet.setAutoFilter(new CellRangeAddress(0,0,0,27));

            fichierExcel.deleteFirstLineContaining("sheet1","AR Historic by client");
            fichierExcel.writeFichierExcel();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void hideUnusefulColumns(Sheet dataSheet) {

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

    private void createColumnNoTax(ExcelFile excelFile, Sheet dataSheet) {
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
                excelFile.evaluateFormulaCell(row.getCell(27));
            }
        }
    }
    @Override
    public String getCommandName() {
        return "formatactivity";
    }
}
