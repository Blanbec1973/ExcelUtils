package org.heyner.excelutils.fusiontrx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
@ExtendWith(OutputCaptureExtension.class)
class FusionTRXTest {
    private final FusionProcessor fusionProcessorMock =  Mockito.mock(FusionProcessor.class);
    @Test
    void fusionTrxTest(CapturedOutput output) {
        FusionTRX fusionTRX = new FusionTRX(fusionProcessorMock);
        doNothing().when(fusionProcessorMock).process(any(),any());
        fusionTRX.execute("arg1","arg2", "arg3");
        assertThat(output.getOut()).contains("FusionTRX completed successfully.");
    }
}
