package org.heyner.excelutils.analyzetrx;

import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.utils.DateTemplateExpander;
import org.heyner.excelutils.utils.DateTemplateExpanderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyzeTRXTest {

    @Mock private AnalyzeTRXConfig cfg;
    @Mock private DateTemplateExpander expander;
    @Mock private ModelCloner cloner;
    @Mock private TrxDataTransfer transfer;
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
        when(transfer.transfer(any(), any(), any(), any())).thenReturn(56);

        // Act
        sut.execute("analyzetrx", "C:/tmp/input.xlsx");

        // Assert – ordre
        InOrder inOrder = inOrder(cloner, transfer, namer);
        inOrder.verify(cloner).copy(
                Paths.get("model/model Analyze TRX.xlsm"),
                Paths.get("target/out/Analyze TRX-2026-01-23.xlsm")
        );
        inOrder.verify(transfer).transfer(
                "C:/tmp/input.xlsx",
                "target/out/Analyze TRX-2026-01-23.xlsm",
                "sheet1",
                "Datas"
        );
        inOrder.verify(namer).renameIfNeeded(
                "target/out/Analyze TRX-2026-01-23.xlsm",
                ExcelConstants.DATAS_SHEET,
                ExcelConstants.TRX_CONTRACT_CELL
        );

        // Pas d'autres interactions
        verifyNoMoreInteractions(cloner, transfer, namer);
    }

    @Test
    void getCommandName_is_stable() {
        assert(sut.getCommandName().equals("analyzetrx"));
    }
}
