package org.heyner.excelutils.directoryparser.processors;

import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationArgs;
import org.heyner.excelutils.application.commands.directoryparser.processors.CorrectionImputationProcessor;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CorrectionImputationProcessorTest
        extends AbstractFileProcessorContractTest<CorrectionImputationProcessor> {

    private final CorrectionImputation correctionImputation = mock(CorrectionImputation.class);

    @Override
    protected CorrectionImputationProcessor newProcessor() {
        return new CorrectionImputationProcessor(correctionImputation);
    }

    @Override
    protected List<Path> matchingSamples() {
        return List.of(
                Path.of(".../UC_PCB_PROJ_TRX_03_1265199083.xlsx"),
                Path.of(".../300000000073327-UC_PCB_PROJ_TRX_03_834070930.xlsx")
        );
    }

    @Override
    protected List<Path> nonMatchingSamples() {
        return List.of(
                Path.of(".../UC_AR_ITEM_ACTIVITY_V1_03.xlsx"),
                Path.of(".../UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx")
        );
    }

    @Test
    void processDelegatesExecutionToCorrectionImputation() throws IOException {
        Path file = Path.of("UC_PCB_PROJ_TRX_03_1265199083.xlsx");
        CorrectionImputationProcessor processor = newProcessor();

        processor.process(file);

        verify(correctionImputation, times(1)).execute(any(CorrectionImputationArgs.class));
    }

    @Test
    void processPassesCorrectFilePathToCorrectionImputation() throws IOException {
        Path file = Path.of("some", "dir", "UC_PCB_PROJ_TRX_03_1265199083.xlsx");
        CorrectionImputationProcessor processor = newProcessor();
        ArgumentCaptor<CorrectionImputationArgs> captor = ArgumentCaptor.forClass(CorrectionImputationArgs.class);

        processor.process(file);

        verify(correctionImputation).execute(captor.capture());
        assertEquals(file, captor.getValue().inputFile());
    }

    @Test
    void processUsesDefaultSheetName() throws IOException {
        Path file = Path.of("UC_PCB_PROJ_TRX_03_1265199083.xlsx");
        CorrectionImputationProcessor processor = newProcessor();
        ArgumentCaptor<CorrectionImputationArgs> captor = ArgumentCaptor.forClass(CorrectionImputationArgs.class);

        processor.process(file);

        verify(correctionImputation).execute(captor.capture());
        assertEquals(ExcelConstants.DEFAULT_SHEET, captor.getValue().sheetName());
    }
}

