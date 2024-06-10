package commun;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FichierExcelTest {


    @BeforeAll
    static void beforeAll() throws IOException {
        new TestInitializerFactory();
    }

    @Test
    void testGetCellValue() throws IOException {
        FichierExcel fichierExcel = new FichierExcel("target/temp/ClasseurTest.xlsx");
        assertEquals("15", fichierExcel.getCellValue("Feuil1","A1"));
        assertEquals("20", fichierExcel.getCellValue("Feuil1","D5"));
        fichierExcel.close();
    }

    @Test
    void testDeleteLine() throws IOException {
        FichierExcel fichierExcel = new FichierExcel("target/temp/SuppressionLigne.xlsx");
        fichierExcel.deleteFirstLineContaining("sheet1","AR Historic by client");
        fichierExcel.writeFichierExcel();
        fichierExcel.close();

        FichierExcel fichierExcel1 = new FichierExcel("target/temp/SuppressionLigne.xlsx");
        assertEquals("From Date", fichierExcel1.getCellValue("sheet1",0,0));
        fichierExcel1.close();

    }


}