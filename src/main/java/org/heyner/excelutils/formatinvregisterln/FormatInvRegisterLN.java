package org.heyner.excelutils.formatinvregisterln;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Slf4j
@Service
public class FormatInvRegisterLN implements CommandService {
    private final FormatInvRegisterLnConfig formatInvRegisterLnConfig;

    public FormatInvRegisterLN(FormatInvRegisterLnConfig formatInvRegisterLnConfig) {
        this.formatInvRegisterLnConfig = formatInvRegisterLnConfig;
    }

    public void execute(String... args) throws IOException {
        try(ExcelFile fichierExcel = ExcelFile.open(args[0])) {
            log.info("File to process : {}", args[0]);

            Sheet dataSheet = fichierExcel.getWorkBook().getSheetAt(0);
            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET,"MS Invoice Register-LN detail");

            log.debug("Last column *"+formatInvRegisterLnConfig.getLastcolumn()+"*");

            for (int i=0; i < formatInvRegisterLnConfig.getLastcolumn()+1 ; i++) {
                if (!formatInvRegisterLnConfig.getNohidecolumns().contains(i)) {
                    dataSheet.setColumnHidden(i,true);
                    log.debug("dans if i="+i);
                }
                log.debug("hors if i="+i);
            }

            createColumnLibelle(fichierExcel, dataSheet);
            createColumnInvReference(fichierExcel,dataSheet);

            fichierExcel.writeFichierExcel();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createColumnLibelle(ExcelFile excelFile, Sheet dataSheet) {
        for (Row row : dataSheet) {
            if (row.getRowNum() == 0) {
                row.createCell(60).setCellValue("Libellé facture");
                CellStyle newCellStyle = row.getCell(59).getCellStyle();
                row.getCell(60).setCellStyle(newCellStyle);
            }
            if (row.getRowNum() > 0) {
                String formula = createFormula(row);
                row.createCell(60).setCellFormula(formula);
                excelFile.evaluateFormulaCell(row.getCell(60));
            }
        }
    }

    private String createFormula(Row row) {
        StringBuilder stb = new StringBuilder("CONCATENATE(");
        int rowNum = row.getRowNum()+1;
        stb.append("AI").append(rowNum).append(", \" \", AJ").append(rowNum);
        stb.append(", \" * \",AK").append(rowNum).append(")");
        return stb.toString();
    }

    private void createColumnInvReference(ExcelFile excelFile, Sheet dataSheet) {
        for (Row row : dataSheet) {
            if (row.getRowNum() == 0) {
                row.createCell(61).setCellValue("InvoiceNumber");
                CellStyle newCellStyle = row.getCell(59).getCellStyle();
                row.getCell(61).setCellStyle(newCellStyle);
            }
            if (row.getRowNum() > 0) {
                int rowNum = row.getRowNum()+1;
                row.createCell(61).setCellFormula("E"+rowNum);
                excelFile.evaluateFormulaCell(row.getCell(61));
            }
        }
    }

    @Override
    public String getCommandName() {
        return "formatinvregisterln";
    }
}
