package org.heyner.excelutils.format_trx;

import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.TestInitializerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormatTRXTest {
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }

    @Test
    void testFormatTrxMain() throws IOException {
        FormatTRX formatTRX = new FormatTRX();
        formatTRX.execute(fileName);

        ExcelFile fichierExcel = ExcelFile.open(fileName);
        assertEquals("Business Unit",fichierExcel.getCellValue(ExcelConstants.DEFAULT_SHEET,0,0));
    }


}