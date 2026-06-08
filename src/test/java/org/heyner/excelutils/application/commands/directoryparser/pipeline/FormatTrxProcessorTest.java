package org.heyner.excelutils.application.commands.directoryparser.pipeline;

import org.heyner.excelutils.application.commands.formattrx.FormatTRX;
import org.heyner.excelutils.application.commands.formattrx.FormatTRXArgs;
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
class FormatTrxProcessorTest
        extends AbstractFileProcessorContractTest<FormatTrxProcessor> {

    private final FormatTRX formatTRX = mock(FormatTRX.class);

    @Override
    protected FormatTrxProcessor newProcessor() {
        return new FormatTrxProcessor(formatTRX);
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
    void processDelegatesExecutionToFormatTRX() throws IOException {
        Path file = Path.of("UC_PCB_PROJ_TRX_03_1265199083.xlsx");
        FormatTrxProcessor processor = newProcessor();

        processor.process(file);

        verify(formatTRX, times(1)).execute(any(FormatTRXArgs.class));
    }

    @Test
    void processPassesCorrectFilePathToFormatTRX() throws IOException {
        Path file = Path.of("some", "dir", "UC_PCB_PROJ_TRX_03_1265199083.xlsx");
        FormatTrxProcessor processor = newProcessor();
        ArgumentCaptor<FormatTRXArgs> captor = ArgumentCaptor.forClass(FormatTRXArgs.class);

        processor.process(file);

        verify(formatTRX).execute(captor.capture());
        assertEquals(file, captor.getValue().inputFile());
    }
}

