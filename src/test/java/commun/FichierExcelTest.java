package commun;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FichierExcelTest {
    private TestInitializerFactory tif = new TestInitializerFactory(this.getClass().getSimpleName());
    private String fileName1 = "target/temp-"+this.getClass().getSimpleName()+"/ClasseurTest.xlsx";
    private String fileName2 = "target/temp-"+this.getClass().getSimpleName()+"/SuppressionLigne.xlsx";

    FichierExcelTest() throws IOException {
    }

    @Test
    void testGetCellValue() throws IOException {
        FichierExcel fichierExcel = new FichierExcel(fileName1);
        assertEquals("15", fichierExcel.getCellValue("Feuil1","A1"));
        assertEquals("20", fichierExcel.getCellValue("Feuil1","D5"));
        fichierExcel.close();
    }

    @Test
    void testDeleteLine() throws IOException {
        FichierExcel fichierExcel = new FichierExcel(fileName2);
        fichierExcel.deleteFirstLineContaining("sheet1","AR Historic by client");
        fichierExcel.writeFichierExcel();
        fichierExcel.close();

        FichierExcel fichierExcel1 = new FichierExcel(fileName2);
        assertEquals("From Date", fichierExcel1.getCellValue("sheet1",0,0));
        fichierExcel1.close();

    }


}