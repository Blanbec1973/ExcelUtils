package org.heyner.excelutils.application.commands.directoryparser.pipeline;

import org.heyner.excelutils.application.commands.formatinvregisterln.FormatInvRegisterLN;
import org.heyner.excelutils.application.commands.formatinvregisterln.FormatInvRegisterLNArgs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormatInvRegisterLnProcessorTest
       extends AbstractFileProcessorContractTest<FormatInvRegisterLnProcessor> {

    private final FormatInvRegisterLN formatInvRegisterLN = mock(FormatInvRegisterLN.class);

    @Override
    protected FormatInvRegisterLnProcessor newProcessor() {
        return new FormatInvRegisterLnProcessor(formatInvRegisterLN);
    }

    @Override
    protected List<Path> matchingSamples() {
        return List.of(
                Path.of(".../UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx"),
                Path.of(".../300000000073327-UC_PCB_MS_INV_REGISTER_LN_03_1790667600.xlsx")
        );
    }

    @Override
    protected List<Path> nonMatchingSamples() {
        return List.of(
                Path.of(".../UC_PCB_PROJ_TRX_03_1265199083.xlsx"),
                Path.of(".../UC_AR_ITEM_ACTIVITY_V1_03.xlsx")
        );
    }

    @Test
    void processDelegatesExecutionToFormatInvRegisterLN() {
        Path file = Path.of("UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx");
        FormatInvRegisterLnProcessor processor = newProcessor();

        processor.process(file);

        verify(formatInvRegisterLN, times(1)).execute(any(FormatInvRegisterLNArgs.class));
    }

    @Test
    void processPassesCorrectFilePathToFormatInvRegisterLN() {
        Path file = Path.of("some", "dir", "UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx");
        FormatInvRegisterLnProcessor processor = newProcessor();
        ArgumentCaptor<FormatInvRegisterLNArgs> captor = ArgumentCaptor.forClass(FormatInvRegisterLNArgs.class);

        processor.process(file);

        verify(formatInvRegisterLN).execute(captor.capture());
        assertEquals(file, captor.getValue().inputFile());
    }
}

