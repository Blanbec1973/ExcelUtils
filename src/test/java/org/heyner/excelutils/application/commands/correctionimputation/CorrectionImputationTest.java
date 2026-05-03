package org.heyner.excelutils.application.commands.correctionimputation;

import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.heyner.excelutils.infrastructure.config.CorrectionImputationConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;

import static org.mockito.Mockito.*;

class CorrectionImputationTest {

    @Test
    void testExecuteSomethingWhenConfigIsTrue() {
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
    void testExecuteDoesNothingWhenConfigIsFalse()  {
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