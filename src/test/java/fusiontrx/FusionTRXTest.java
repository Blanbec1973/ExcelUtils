package fusiontrx;


import commun.TestInitializerFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.ExcelFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FusionTRXTest {

    @BeforeEach
    void setUp() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
        File f = new File ("target/temp-FusionTRXTest/fusion/fusionTRX.xlsx");
        if (f.exists())
            f.delete();
    }

    @Test
    void testFusionTRX() throws IOException {
        new FusionTRX(new String[] {"FUSION_TRX", "target/temp-FusionTRXTest/fusion/",
                                                        "target/temp-FusionTRXTest/fusion/"});

        ExcelFile fichierExcel = new ExcelFile("target/temp-FusionTRXTest/fusion/fusionTRX.xlsx");
        int rowNum = 0;
        float foreignAmountCum = 0;
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet("Fusion");

        for (Row row : dataSheet) {
            rowNum+=1;
            if (row.getRowNum() !=0) {
                foreignAmountCum+=row.getCell(29).getNumericCellValue();
            }
        }
        assertEquals(12,rowNum);
        assertEquals(630260,Math.round(100*foreignAmountCum)/100);
    }




}