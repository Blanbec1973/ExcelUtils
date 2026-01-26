package org.heyner.excelutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelConstantsTest {
    @Test
    void testConstantsValues() {
        assertEquals("sheet1", ExcelConstants.DEFAULT_SHEET);
        assertEquals(29, ExcelConstants.FOREIGN_AMOUNT_COLUMN);
    }
}