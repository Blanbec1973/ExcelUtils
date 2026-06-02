package org.heyner.excelutils.fusiontrx;

import org.heyner.excelutils.application.commands.fusiontrx.FusionProcessor;
import org.heyner.excelutils.application.commands.fusiontrx.FusionTRX;
import org.heyner.excelutils.application.commands.fusiontrx.FusionTRXArgs;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

class FusionTRXTest {
    private final FusionProcessor fusionProcessorMock =  Mockito.mock(FusionProcessor.class);

    @Test
    void fusionTrxTest() {
        FusionTRX fusionTRX = new FusionTRX(fusionProcessorMock);
        doNothing().when(fusionProcessorMock).process(any(),any());
        fusionTRX.execute(new FusionTRXArgs(Path.of("arg2"), Path.of("arg3")));
        verify(fusionProcessorMock).process("arg2", "arg3");
    }
}
