package org.heyner.excelutils.application.commands.correctionimputation;

import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.heyner.excelutils.infrastructure.config.CorrectionImputationConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

class CorrectionImputationTest {
//    @BeforeEach
//    void setUp() throws IOException {
//        Path path = Paths.get("target/test/correctionImputation");
//        if (Files.exists(path))
//            FileUtils.cleanDirectory(new File("target/test/correctionImputation"));
//        FileUtils.copyDirectory(new File ("src/test/resources/temp/"), new File("target/test/correctionImputation/"));
//    }

//    @Test
//    void testCorrectionImputation() throws IOException {
//
//        CorrectionImputationConfig correctionImputationConfig = mock(CorrectionImputationConfig.class);
//        when(correctionImputationConfig.isCorrectionImputationActionEnabled()).thenReturn(true);
//
//        CorrectionImputationAdapter service = new CorrectionImputationAdapter(new CorrectionImputationRule());
//        CorrectionImputationArgs args = CorrectionImputationArgs.builder()
//                .inputFile(Path.of("target/test/correctionImputation/TrxToCorrect.xlsx"))
//                .sheetName(ExcelConstants.DEFAULT_SHEET)
//                .build();
//        CorrectionImputation correctionImputation = new CorrectionImputation(correctionImputationConfig, service);
//        correctionImputation.execute(args);
//
//        ExcelFile fichierExcel = ExcelFile.open("target/test/correctionImputation/TrxToCorrect.xlsx");
//        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(ExcelConstants.DEFAULT_SHEET);
//        String formula = dataSheet.getRow(10).getCell(56).getCellFormula();
//        assertEquals("AC11/8", formula);
//        fichierExcel.close();
//    }

    @Test
    void testExecuteSomethingWhenConfigIsTrue() throws IOException {
        // Mock de la configuration
        CorrectionImputationConfig configMock = mock(CorrectionImputationConfig.class);
        when(configMock.isCorrectionImputationActionEnabled()).thenReturn(true);

        // Mock du port
        CorrectionImputationPort portMock = mock(CorrectionImputationPort.class);

        CorrectionImputation correctionImputation = new CorrectionImputation(configMock, portMock);

        // Appel de la méthode avec des arguments fictifs
        CorrectionImputationArgs args = CorrectionImputationArgs.builder()
                .inputFile(Path.of("dummy.xlsx"))
                .sheetName(ExcelConstants.DEFAULT_SHEET)
                .build();
        correctionImputation.execute(args);

        // Vérifie que le service n'est jamais appelé
        verify(portMock, Mockito.times(1)).correct(any(), any());
    }
    @Test
    void testExecuteDoesNothingWhenConfigIsFalse() throws IOException {
        // Mock de la configuration
        CorrectionImputationConfig configMock = mock(CorrectionImputationConfig.class);
        when(configMock.isCorrectionImputationActionEnabled()).thenReturn(false);

        // Mock du port
        CorrectionImputationPort portMock = mock(CorrectionImputationPort.class);

        CorrectionImputation correctionImputation = new CorrectionImputation(configMock, portMock);

        // Appel de la méthode avec des arguments fictifs
        CorrectionImputationArgs args = CorrectionImputationArgs.builder()
                .inputFile(Path.of("dummy.xlsx"))
                .sheetName(ExcelConstants.DEFAULT_SHEET)
                .build();
        correctionImputation.execute(args);

        // Vérifie que le service n'est jamais appelé
        verify(portMock, never()).correct(any(), any());
    }
}