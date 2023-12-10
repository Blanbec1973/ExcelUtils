package correctionimputation;

import commun.FichierExcel;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CorrectionImputationTest {
    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("target/test/correctionImputation");
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File("target/test/correctionImputation"));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"), new File("target/test/correctionImputation/"));
    }

    @Test
    void testCorrectionImputation() throws IOException, InterruptedException {
        CorrectionImputation c = new CorrectionImputation();
        c.correctionImputation(new String[] {"target/test/correctionImputation/TrxToCorrect.xlsx", "sheet1"});

        FichierExcel fichierExcel = new FichierExcel("target/test/correctionImputation/TrxToCorrect.xlsx");
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet("sheet1");
        String formula = dataSheet.getRow(10).getCell(56).getCellFormula();
        assertEquals("AC11/8", formula);
        fichierExcel.close();
    }
}