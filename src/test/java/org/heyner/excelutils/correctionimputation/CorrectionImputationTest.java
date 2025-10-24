package org.heyner.excelutils.correctionimputation;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.ExcelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CorrectionImputationTest {
    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("target/test/correctionImputation");
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File("target/test/correctionImputation"));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"), new File("target/test/correctionImputation/"));
    }

    @Test
    void testCorrectionImputation() throws IOException {

        CorrectionImputationConfig correctionImputationConfig = mock(CorrectionImputationConfig.class);
        when(correctionImputationConfig.isCorrectionImputationActionEnabled()).thenReturn(true);

        String [] args = new String[] {"target/test/correctionImputation/TrxToCorrect.xlsx", ExcelConstants.DEFAULT_SHEET};
        CorrectionImputation correctionImputation = new CorrectionImputation(correctionImputationConfig);
        correctionImputation.execute(args);

        ExcelFile fichierExcel = new ExcelFile("target/test/correctionImputation/TrxToCorrect.xlsx");
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(ExcelConstants.DEFAULT_SHEET);
        String formula = dataSheet.getRow(10).getCell(56).getCellFormula();
        assertEquals("AC11/8", formula);
        fichierExcel.close();
    }
    @Test
    void testExecuteDoesNothingWhenConfigIsFalse() throws IOException {
        // Mock de la configuration
        CorrectionImputationConfig configMock = mock(CorrectionImputationConfig.class);
        when(configMock.isCorrectionImputationActionEnabled()).thenReturn(false);

        // Spy de la classe à tester
        CorrectionImputation correctionImputation = Mockito.spy(new CorrectionImputation(configMock));

        // Appel de la méthode avec des arguments fictifs
        correctionImputation.execute("dummy.xlsx", ExcelConstants.DEFAULT_SHEET);

        // Vérifie que certaines méthodes internes ne sont jamais appelées
        verify(correctionImputation, never()).processRow(any());
    }
}