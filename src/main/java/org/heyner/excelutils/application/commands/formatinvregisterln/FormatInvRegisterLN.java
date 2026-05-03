package org.heyner.excelutils.application.commands.formatinvregisterln;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.infrastructure.config.FormatInvRegisterLnConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormatInvRegisterLN implements Command<FormatInvRegisterLNArgs> {
    private final FormatInvRegisterLnConfig formatInvRegisterLnConfig;

    private static final String FILE_TO_PROCESS_LOG = "Formatting invoice register LN file : {}";
    private static final String ERROR_PROCESSING_FILE_LOG = "Error processing file: {}";

    public void execute(FormatInvRegisterLNArgs args) {
        try(ExcelFile fichierExcel = ExcelFile.open(args.inputFile().toString())) {
            log.info(FILE_TO_PROCESS_LOG, args.inputFile());

            Sheet dataSheet = fichierExcel.getWorkBook().getSheetAt(0);
            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET,"MS Invoice Register-LN detail");

            log.debug("Configuration: lastcolumn={}, nohidecolumns={}", formatInvRegisterLnConfig.getLastcolumn(), formatInvRegisterLnConfig.getNohidecolumns());


            IntStream.rangeClosed(0, formatInvRegisterLnConfig.getLastcolumn())
                    .filter(i -> !formatInvRegisterLnConfig.getNoHideSet().contains(i))
                            .forEach(i -> dataSheet.setColumnHidden(i, true));

            log.debug("Creating 'Libellé facture' and 'InvoiceNumber' columns");
            createColumnLibelle(fichierExcel, dataSheet);
            createColumnInvReference(fichierExcel,dataSheet);

            fichierExcel.writeFichierExcel();
            log.info("Invoice register LN file formatted successfully: {}", args.inputFile());
        } catch (IOException e) {
            log.error(ERROR_PROCESSING_FILE_LOG, args.inputFile(), e);
            throw new FatalApplicationException("Error processing file: " + args.inputFile(),
                    e, ExitCodes.FILE_PROCESSING_ERROR);
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
    public String name() {
        return "formatinvregisterln";
    }

    @Override
    public FormatInvRegisterLNArgs parse(String[] args) {
        return new FormatInvRegisterLNArgs(Path.of(args[1]));
    }
}
