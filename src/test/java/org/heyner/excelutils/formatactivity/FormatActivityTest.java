package org.heyner.excelutils.formatactivity;

import org.heyner.common.ExcelFile;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.TestInitializerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormatActivityTest {
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }
    @Test
    void execute() throws IOException {
        FormatActivity formatActivity = new FormatActivity();
        formatActivity.execute(fileName);

        ExcelFile fichierExcel = new ExcelFile(fileName);
        assertEquals("From Date",fichierExcel.getCellValue(ExcelConstants.DEFAULT_SHEET,0,0));
        assertEquals(15, fichierExcel.rowCount(ExcelConstants.DEFAULT_SHEET,0));
        assertEquals("Mt HT", fichierExcel.getCellValue(ExcelConstants.DEFAULT_SHEET,"AB1"));
    }

    @Test
    void getCommandName() {
        FormatActivity formatActivity = new FormatActivity();
        assertEquals("formatactivity",formatActivity.getCommandName());
    }
}