package commun;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FichierExcelTest {
    private static FichierExcel fichierExcel;

    @BeforeAll
    static void beforeAll() throws IOException {
        new TestInitializerFactory();
        fichierExcel = new FichierExcel("target/temp/ClasseurTest.xlsx");
    }

    @AfterAll
    static void afterAll() throws IOException {
        fichierExcel.close();
    }

    @Test
    void getCellValue() {
        assertEquals("15", fichierExcel.getCellValue("Feuil1","A1"));
        assertEquals("20", fichierExcel.getCellValue("Feuil1","D5"));
    }
}