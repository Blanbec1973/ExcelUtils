package commun;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FichierExcelTest {
    private final String fileName1 = "target/temp-"+this.getClass().getSimpleName()+"/ClasseurTest.xlsx";
    private final String fileName2 = "target/temp-"+this.getClass().getSimpleName()+"/SuppressionLigne.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }

    @Test
    void testGetCellValue() throws IOException {
        FichierExcel fichierExcel = new FichierExcel(fileName1);
        assertEquals("15", fichierExcel.getCellValue("Feuil1","A1"));
        assertEquals("20", fichierExcel.getCellValue("Feuil1","D5"));
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

    @Test
    void testRoxCount() throws IOException {
        FichierExcel fichierExcel = new FichierExcel(fileName1);
        assertEquals(5,fichierExcel.rowCount("Feuil1",3));
    }


}