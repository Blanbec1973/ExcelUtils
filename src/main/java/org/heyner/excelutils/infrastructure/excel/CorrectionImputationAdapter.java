package org.heyner.excelutils.infrastructure.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationRule;
import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class CorrectionImputationAdapter implements CorrectionImputationPort {

    private final CorrectionImputationRule rule;

    @Override
    public void correct(Path inputFile, String sheetName) {
        try (ExcelFile excelFile = ExcelFile.open(inputFile.toString())) {
            Sheet sheet = excelFile.getWorkBook().getSheet(sheetName);
            for (Row row : sheet) {
                log.debug("Evaluating row {}", row.getRowNum());
                Cell cell = row.getCell(56);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String label = row.getCell(56).getStringCellValue();
                    String code = row.getCell(8).getStringCellValue();

                    if (rule.shouldCorrect(label, code)) {
                        log.debug("Applying correction on row {}", row.getRowNum());
                        int rowNum = row.getRowNum() + 1;
                        String formula = rule.formulaFor(rowNum);
                        row.getCell(56).setCellFormula(formula);
                        excelFile.evaluateFormulaCell(row.getCell(56));
                    }
                }
            }
            excelFile.writeFichierExcel();
        } catch (IOException e) {
            throw new FatalApplicationException(
                    "Error processing file: " + inputFile,
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR
            );
        }
    }
}