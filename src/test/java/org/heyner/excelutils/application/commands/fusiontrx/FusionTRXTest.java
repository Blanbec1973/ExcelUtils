package org.heyner.excelutils.application.commands.fusiontrx;

import org.heyner.excelutils.cli.Slf4jCliPrinter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FusionTRXTest {
    private final FusionProcessor fusionProcessorMock =  Mockito.mock(FusionProcessor.class);

    @Test
    void fusionTrxTest() {
        FusionTRX fusionTRX = new FusionTRX(fusionProcessorMock, mock(Slf4jCliPrinter.class));
        doNothing().when(fusionProcessorMock).process(any(),any());
        fusionTRX.execute(new FusionTRXArgs(Path.of("arg2"), Path.of("arg3")));
        verify(fusionProcessorMock).process("arg2", "arg3");
    }
}
