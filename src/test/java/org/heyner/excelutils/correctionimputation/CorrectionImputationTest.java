package org.heyner.excelutils.correctionimputation;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationArgs;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationService;
import org.heyner.excelutils.infrastructure.config.CorrectionImputationConfig;
import org.heyner.excelutils.infrastructure.excel.CorrectionImputationAdapter;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        CorrectionImputationAdapter service = new CorrectionImputationAdapter(new CorrectionImputationService());
        CorrectionImputationArgs args = CorrectionImputationArgs.builder()
                .inputFile(Path.of("target/test/correctionImputation/TrxToCorrect.xlsx"))
                .sheetName(ExcelConstants.DEFAULT_SHEET)
                .build();
        CorrectionImputation correctionImputation = new CorrectionImputation(correctionImputationConfig, service);
        correctionImputation.execute(args);

        ExcelFile fichierExcel = ExcelFile.open("target/test/correctionImputation/TrxToCorrect.xlsx");
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

        // Mock du service
        CorrectionImputationAdapter serviceMock = mock(CorrectionImputationAdapter.class);

        CorrectionImputation correctionImputation = new CorrectionImputation(configMock, serviceMock);

        // Appel de la méthode avec des arguments fictifs
        CorrectionImputationArgs args = CorrectionImputationArgs.builder()
                .inputFile(Path.of("dummy.xlsx"))
                .sheetName(ExcelConstants.DEFAULT_SHEET)
                .build();
        correctionImputation.execute(args);

        // Vérifie que le service n'est jamais appelé
        verify(serviceMock, never()).processRow(any(), any());
    }
}