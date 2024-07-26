package format_trx;

import commun.FichierExcel;
import commun.TestInitializerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormatTRXTest {
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }

    @Test
    void testFormatTrxMain() throws IOException {
        FormatTRX.applyFormatTRX(new String[]{fileName});

        FichierExcel fichierExcel = new FichierExcel(fileName);
        assertEquals("Business Unit",fichierExcel.getCellValue("sheet1",0,0));
    }


}