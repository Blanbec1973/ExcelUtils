package org.heyner.excelutils.directoryparser.processors;

import org.heyner.excelutils.application.commands.directoryparser.processors.FormatActivityProcessor;
import org.heyner.excelutils.application.commands.formatactivity.FormatActivity;
import org.heyner.excelutils.application.commands.formatactivity.FormatActivityArgs;
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
class FormatActivityProcessorTest
        extends AbstractFileProcessorContractTest<FormatActivityProcessor> {

    private final FormatActivity formatActivity = mock(FormatActivity.class);

    @Override
    protected FormatActivityProcessor newProcessor() {
        return new FormatActivityProcessor(formatActivity);
    }

    @Override
    protected List<Path> matchingSamples() {
        return List.of(
                Path.of(".../UC_AR_ITEM_ACTIVITY_V1_03.xlsx"),
                Path.of(".../300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx")
        );
    }

    @Override
    protected List<Path> nonMatchingSamples() {
        return List.of(
                Path.of(".../UC_PCB_PROJ_TRX_03_1265199083.xlsx"),
                Path.of(".../UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx")
        );
    }

    @Test
    void processDelegatesExecutionToFormatActivity() throws IOException {
        Path file = Path.of("UC_AR_ITEM_ACTIVITY_V1_03.xlsx");
        FormatActivityProcessor processor = newProcessor();

        processor.process(file);

        verify(formatActivity, times(1)).execute(any(FormatActivityArgs.class));
    }

    @Test
    void processPassesCorrectFilePathToFormatActivity() throws IOException {
        Path file = Path.of("some", "dir", "UC_AR_ITEM_ACTIVITY_V1_03.xlsx");
        FormatActivityProcessor processor = newProcessor();
        ArgumentCaptor<FormatActivityArgs> captor = ArgumentCaptor.forClass(FormatActivityArgs.class);

        processor.process(file);

        verify(formatActivity).execute(captor.capture());
        assertEquals(file, captor.getValue().inputFile());
    }
}

