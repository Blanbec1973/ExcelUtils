package format_trx;

import commun.FichierExcel;
import commun.TestInitializerFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FormatTRXTest {
    private TestInitializerFactory tif = new TestInitializerFactory(this.getClass().getSimpleName());
    private String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    FormatTRXTest() throws IOException {
    }


    @Test
    void testFormatTrxMain() throws IOException {
        FormatTRX formatTRX = new FormatTRX();
        formatTRX.main(new String[]{fileName});

        FichierExcel fichierExcel = new FichierExcel(fileName);
        assertEquals("Business Unit",fichierExcel.getCellValue("sheet1",0,0));

    }


}