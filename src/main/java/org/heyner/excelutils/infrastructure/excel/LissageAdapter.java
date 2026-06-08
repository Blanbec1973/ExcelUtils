package org.heyner.excelutils.infrastructure.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.lissage.LissageCalculator;
import org.heyner.excelutils.application.ports.LissagePort;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.exitcode.ExitCodes;
import org.heyner.excelutils.shared.exception.FatalApplicationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LissageAdapter implements LissagePort {

    private static final String START_LOG = "Starting lissage on file {} row {} with target margin {}";
    private static final String DONE_LOG = "Lissage completed on file {} row {}";

    private final LissageCalculator calculator;

    @Override
    public void smooth(Path inputFile, int lissageRow, double targetMargin) {
        log.info(START_LOG, inputFile, lissageRow, targetMargin);

        int rowIndex = lissageRow - 1;
        if (rowIndex < 0) {
            throw new IllegalArgumentException("lissageRow must be >= 1");
        }

        try (ExcelFile excelFile = ExcelFile.open(inputFile.toString())) {
            Sheet extRevSheet = getSheetOrThrow(excelFile, ExcelConstants.LISSAGE_EXT_REV_SHEET);
            Sheet billingSheet = getSheetOrThrow(excelFile, ExcelConstants.LISSAGE_BILLING_SHEET);

            Row billingRow = getRowOrCreate(billingSheet, rowIndex);

            List<Integer> extRevColumns = extRevColumns();
            List<Integer> billingColumns = billingColumns();
            double pastCumulative = readNumericCell(extRevSheet,
                    ExcelConstants.LISSAGE_EXT_REV_PAST_CUMULATED_ROW,
                    ExcelConstants.LISSAGE_EXT_REV_PAST_CUMULATED_COLUMN);

            for (int i = 0; i < extRevColumns.size(); i++) {
                double cost = sumCostForColumn(extRevSheet, extRevColumns.get(i));
                double revenue = calculator.revenueForTargetMargin(cost, targetMargin);
                if (i == 0) {
                    // Current month must be realigned against historical cumulative already posted.
                    revenue -= pastCumulative;
                }
                setNumericCell(billingRow, billingColumns.get(i), revenue);
            }

            String formula = "-SUM(I" + lissageRow + ":U" + lissageRow + ")";
            Cell balanceCell = billingRow.getCell(ExcelConstants.LISSAGE_BILLING_BALANCE_COLUMN);
            if (balanceCell == null) {
                balanceCell = billingRow.createCell(ExcelConstants.LISSAGE_BILLING_BALANCE_COLUMN);
            }
            balanceCell.setCellFormula(formula);
            // Force Excel à tout recalculer à la prochaine ouverture du fichier
            excelFile.getWorkBook().setForceFormulaRecalculation(true);

            excelFile.writeFichierExcel();
            log.info(DONE_LOG, inputFile, lissageRow);
        } catch (IOException e) {
            log.error("ERROR: Fatal error while processing {}", inputFile, e);
            throw new FatalApplicationException(
                    inputFile.toString(),
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR
            );
        }
    }

    private static Row getRowOrCreate(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return row == null ? sheet.createRow(rowIndex) : row;
    }

    private static Sheet getSheetOrThrow(ExcelFile excelFile, String sheetName) {
        Sheet sheet = excelFile.getWorkBook().getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        return sheet;
    }

    private static void setNumericCell(Row row, int column, double value) {
        Cell cell = row.getCell(column);
        if (cell == null) {
            cell = row.createCell(column);
        }
        // Supprimer la formule avant d'écrire la valeur.
        // Sans ce removeFormula(), Excel recalcule la formule à l'ouverture
        // du fichier et écrase silencieusement la valeur écrite par POI.
        if (cell.getCellType() == CellType.FORMULA) {
            cell.removeFormula();
        }
        cell.setCellValue(value);
    }

    private static double sumCostForColumn(Sheet sheet, int column) {
        double total = 0.0d;
        for (int rowNumber = ExcelConstants.LISSAGE_EXT_REV_COST_START_ROW;
             rowNumber <= ExcelConstants.LISSAGE_EXT_REV_COST_END_ROW;
             rowNumber++) {
            Row row = sheet.getRow(rowNumber - 1);
            if (row != null) {
                total += readNumericCell(row, column);
            }
        }
        return total;
    }

    private static double readNumericCell(Row row, int column) {
        Cell cell = row.getCell(column);
        if (cell == null) {
            return 0.0d;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }

        if (cell.getCellType() == CellType.FORMULA) {
            return switch (cell.getCachedFormulaResultType()) {
                case NUMERIC -> cell.getNumericCellValue();
                case STRING -> parseStringNumber(cell.getStringCellValue());
                default -> 0.0d;
            };
        }

        if (cell.getCellType() == CellType.STRING) {
            return parseStringNumber(cell.getStringCellValue());
        }

        return 0.0d;
    }

    private static double parseStringNumber(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0.0d;
        }
        String normalized = raw.trim().replace(" ", "").replace(',', '.');
        return Double.parseDouble(normalized);
    }

    private static double readNumericCell(Sheet sheet, int rowNumber, int column) {
        Row row = sheet.getRow(rowNumber - 1);
        if (row == null) {
            return 0.0d;
        }
        return readNumericCell(row, column);
    }

    private static List<Integer> extRevColumns() {
        List<Integer> columns = new ArrayList<>(13);
        columns.add(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN);
        for (int col = ExcelConstants.LISSAGE_EXT_REV_NEXT_MONTH_START_COLUMN;
             col <= ExcelConstants.LISSAGE_EXT_REV_NEXT_MONTH_END_COLUMN;
             col++) {
            columns.add(col);
        }
        return columns;
    }

    private static List<Integer> billingColumns() {
        List<Integer> columns = new ArrayList<>(13);
        columns.add(ExcelConstants.LISSAGE_BILLING_CURRENT_MONTH_COLUMN);
        for (int col = ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_START_COLUMN;
             col <= ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_END_COLUMN;
             col++) {
            columns.add(col);
        }
        return columns;
    }
}
