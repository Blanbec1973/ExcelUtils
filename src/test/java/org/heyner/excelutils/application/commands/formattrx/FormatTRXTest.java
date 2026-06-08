package org.heyner.excelutils.application.commands.formattrx;

import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.TestInitializerFactory;
import org.heyner.excelutils.cli.Slf4jCliPrinter;
import org.heyner.excelutils.infrastructure.excel.FormatTRXAdapter;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormatTRXTest {
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }

    @Test
    void testFormatTrxMain() {
        FormatTRX formatTRX = new FormatTRX(new FormatTRXAdapter(), mock(Slf4jCliPrinter.class));
        formatTRX.execute(new FormatTRXArgs(Path.of(fileName)));

        try (ExcelFile fichierExcel = ExcelFile.open(fileName)) {
            assertEquals("Business Unit", fichierExcel.getCellValue(ExcelConstants.DEFAULT_SHEET, 0, 0));
        } catch (IOException e) {
            fail("Error reading the formatted file: " + fileName, e);
        }
    }
}