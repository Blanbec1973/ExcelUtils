package org.heyner.excelutils.utils;

import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.TestInitializerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelPrefixReaderTest {
    private final String trxFileName = "UC_PCB_PROJ_TRX_03_1265199083.xlsx";
    private final String trxPath = "target/temp-" + this.getClass().getSimpleName() + "/";

    @BeforeEach
    void setUp() throws IOException {
        System.out.println("icila : " + this.getClass().getSimpleName());
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }
    @Test
    void readTrxTest() {
        ExcelPrefixReader excelPrefixReader = new ExcelPrefixReader();

        String prefix = excelPrefixReader.read(trxPath+trxFileName, ExcelConstants.DEFAULT_SHEET,
                ExcelConstants.TRX_CONTRACT_CELL);

        assertEquals("300000000073327", prefix);
    }
}
