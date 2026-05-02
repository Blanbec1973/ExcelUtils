package org.heyner.excelutils.analyzetrx;

import org.heyner.excelutils.application.commands.analyzetrx.AnalyzeTRX;
import org.heyner.excelutils.application.commands.analyzetrx.AnalyzeTRXArgs;
import org.heyner.excelutils.application.commands.analyzetrx.ModelCloner;
import org.heyner.excelutils.infrastructure.config.AnalyzeTRXConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.application.ports.ExcelTransferPort;
import org.heyner.excelutils.shared.utils.DateTemplateExpander;
import org.heyner.excelutils.shared.utils.filenaming.ResultNamer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyzeTRXTest {

    @Mock private AnalyzeTRXConfig cfg;
    @Mock private DateTemplateExpander expander;
    @Mock private ModelCloner cloner;
    @Mock private ExcelTransferPort excelTransfer;
    @Mock private ResultNamer namer;

    @InjectMocks
    private AnalyzeTRX sut; // construit via le constructeur

    @Test
    void execute_happy_path_calls_collaborators_in_order_with_expected_params() {
        //arrange
        when(cfg.getPathModel()).thenReturn("model/model Analyze TRX.xlsm");
        when(cfg.getPathResultFile()).thenReturn("target/out/Analyze TRX-aaaa-mm-jj.xlsm");
        when(cfg.getSheetIn()).thenReturn("sheet1");
        when(cfg.getSheetOut()).thenReturn("Datas");

        when(expander.expand("target/out/Analyze TRX-aaaa-mm-jj.xlsm"))
                .thenReturn("target/out/Analyze TRX-2026-01-23.xlsm");
        when(excelTransfer.transfer(any(), any(), any(), any())).thenReturn(56);

        // Act
        sut.execute(new AnalyzeTRXArgs(Path.of("C:/tmp/input.xlsx")));

        // Assert – ordre
        InOrder inOrder = inOrder(cloner, excelTransfer, namer);
        inOrder.verify(cloner).copy(
                Paths.get("model/model Analyze TRX.xlsm"),
                Paths.get("target/out/Analyze TRX-2026-01-23.xlsm")
        );
        inOrder.verify(excelTransfer).transfer(
                Path.of("C:/tmp/input.xlsx"),
                Path.of("target/out/Analyze TRX-2026-01-23.xlsm"),
                "sheet1",
                "Datas"
        );
        inOrder.verify(namer).renameIfNeeded(
                Path.of("target/out/Analyze TRX-2026-01-23.xlsm"),
                ExcelConstants.DATAS_SHEET,
                ExcelConstants.TRX_CONTRACT_CELL
        );

        // Pas d'autres interactions
        verifyNoMoreInteractions(cloner, excelTransfer, namer);
    }

    @Test
    void getCommandName_is_stable() {
        assertEquals("analyzetrx", sut.getCommandName());
    }
}
