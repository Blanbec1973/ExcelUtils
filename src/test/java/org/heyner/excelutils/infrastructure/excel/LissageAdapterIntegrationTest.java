package org.heyner.excelutils.infrastructure.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.lissage.LissageCalculator;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LissageAdapterIntegrationTest {

    @Test
    void smooth_writes_revenue_on_billing_and_balance_formula() throws Exception {
        Path tempDir = Files.createTempDirectory("lissage-it-");
        Path file = tempDir.resolve("lissage.xlsx");

        createWorkbook(file);

        LissageAdapter adapter = new LissageAdapter(new LissageCalculator());
        adapter.smooth(file, 2, 0.25d);

        try (ExcelFile excelFile = ExcelFile.open(file.toString())) {
            Sheet billing = excelFile.getWorkBook().getSheet(ExcelConstants.LISSAGE_BILLING_SHEET);
            Row row = billing.getRow(1);

            assertEquals(83.3333333333d, row.getCell(ExcelConstants.LISSAGE_BILLING_CURRENT_MONTH_COLUMN).getNumericCellValue(), 1.0e-6);
            assertEquals(146.6666666667d, row.getCell(ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_START_COLUMN).getNumericCellValue(), 1.0e-6);
            assertEquals(293.3333333333d, row.getCell(ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_END_COLUMN).getNumericCellValue(), 1.0e-6);
            assertEquals("-SUM(I2:U2)", row.getCell(ExcelConstants.LISSAGE_BILLING_BALANCE_COLUMN).getCellFormula());
        }
    }

    private static void createWorkbook(Path file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); OutputStream out = Files.newOutputStream(file)) {
            Sheet extRev = workbook.createSheet(ExcelConstants.LISSAGE_EXT_REV_SHEET);
            Sheet billing = workbook.createSheet(ExcelConstants.LISSAGE_BILLING_SHEET);

            Row billingRow = billing.createRow(1);
            Row extRevPastRow = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_PAST_CUMULATED_ROW - 1);
            Row extRevCostRow51 = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_COST_START_ROW - 1);
            Row extRevCostRow52 = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_COST_START_ROW);
            Row extRevCostRow53 = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_COST_END_ROW - 1);

            extRevPastRow.createCell(ExcelConstants.LISSAGE_EXT_REV_PAST_CUMULATED_COLUMN).setCellValue(50.0d);

            extRevCostRow51.createCell(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN).setCellValue(30.0d);
            extRevCostRow52.createCell(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN).setCellValue(40.0d);
            extRevCostRow53.createCell(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN).setCellValue(30.0d);

            double value = 110.0d;
            for (int col = ExcelConstants.LISSAGE_EXT_REV_NEXT_MONTH_START_COLUMN;
                 col <= ExcelConstants.LISSAGE_EXT_REV_NEXT_MONTH_END_COLUMN;
                 col++) {
                extRevCostRow51.createCell(col).setCellValue(value * 0.2d);
                extRevCostRow52.createCell(col).setCellValue(value * 0.3d);
                extRevCostRow53.createCell(col).setCellValue(value * 0.5d);
                value += 10.0d;
            }

            billingRow.createCell(ExcelConstants.LISSAGE_BILLING_CURRENT_MONTH_COLUMN).setCellValue(0.0d);
            for (int col = ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_START_COLUMN;
                 col <= ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_END_COLUMN;
                 col++) {
                billingRow.createCell(col).setCellValue(0.0d);
            }

            workbook.write(out);
        }
    }

    @Test
    void smooth_overwrites_formula_with_calculated_value() throws Exception {
        Path tempDir = Files.createTempDirectory("lissage-it-formula-");
        Path file = tempDir.resolve("lissage-formula.xlsx");

        createWorkbookWithFormulas(file);

        LissageAdapter adapter = new LissageAdapter(new LissageCalculator());
        adapter.smooth(file, 2, 0.25d);

        try (ExcelFile excelFile = ExcelFile.open(file.toString())) {
            Sheet billing = excelFile.getWorkBook().getSheet(ExcelConstants.LISSAGE_BILLING_SHEET);
            Row row = billing.getRow(1);

            Cell currentMonthCell = row.getCell(ExcelConstants.LISSAGE_BILLING_CURRENT_MONTH_COLUMN);
            assertNotEquals(CellType.FORMULA, currentMonthCell.getCellType(),
                    "La cellule du mois en cours ne doit plus être une formule");
            assertEquals(83.3333333333d, currentMonthCell.getNumericCellValue(), 1.0e-6);

            Cell nextMonthCell = row.getCell(ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_START_COLUMN);
            assertNotEquals(CellType.FORMULA, nextMonthCell.getCellType(),
                    "La cellule du mois suivant ne doit plus être une formule");
        }
    }

    private static void createWorkbookWithFormulas(Path file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); OutputStream out = Files.newOutputStream(file)) {
            Sheet extRev = workbook.createSheet(ExcelConstants.LISSAGE_EXT_REV_SHEET);
            Sheet billing = workbook.createSheet(ExcelConstants.LISSAGE_BILLING_SHEET);

            Row billingRow = billing.createRow(1);
            Row extRevPastRow = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_PAST_CUMULATED_ROW - 1);
            Row extRevCostRow51 = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_COST_START_ROW - 1);
            Row extRevCostRow52 = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_COST_START_ROW);
            Row extRevCostRow53 = extRev.createRow(ExcelConstants.LISSAGE_EXT_REV_COST_END_ROW - 1);

            extRevPastRow.createCell(ExcelConstants.LISSAGE_EXT_REV_PAST_CUMULATED_COLUMN).setCellValue(50.0d);
            extRevCostRow51.createCell(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN).setCellValue(30.0d);
            extRevCostRow52.createCell(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN).setCellValue(40.0d);
            extRevCostRow53.createCell(ExcelConstants.LISSAGE_EXT_REV_CURRENT_MONTH_COST_COLUMN).setCellValue(30.0d);

            double value = 110.0d;
            for (int col = ExcelConstants.LISSAGE_EXT_REV_NEXT_MONTH_START_COLUMN;
                 col <= ExcelConstants.LISSAGE_EXT_REV_NEXT_MONTH_END_COLUMN; col++) {
                extRevCostRow51.createCell(col).setCellValue(value * 0.2d);
                extRevCostRow52.createCell(col).setCellValue(value * 0.3d);
                extRevCostRow53.createCell(col).setCellValue(value * 0.5d);
                value += 10.0d;
            }

            // Billing : cellules contenant des formules (cas réel à écraser)
            billingRow.createCell(ExcelConstants.LISSAGE_BILLING_CURRENT_MONTH_COLUMN)
                    .setCellFormula("SUM(A1:A10)");
            for (int col = ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_START_COLUMN;
                 col <= ExcelConstants.LISSAGE_BILLING_NEXT_MONTH_END_COLUMN; col++) {
                billingRow.createCell(col).setCellFormula("SUM(A1:A10)");
            }

            workbook.write(out);
        }
    }
}

