package org.heyner.excelutils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ExcelUtils.class,
        ApplicationProperties.class,
        CustomExitCodeGenerator.class
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExcelUtilsTest {

    @Autowired
    private ExcelUtils excelUtils;

    @Autowired
    private CustomExitCodeGenerator exitCodeGenerator;

    @ExtendWith(OutputCaptureExtension.class)
    @Test
    void testRunWithInvalidCommand(CapturedOutput output) {
        String[] args = {"invalidCommand"};

        excelUtils.run(args);

        // Vérifie que le code de sortie est bien celui d'erreur
        assertEquals(2, exitCodeGenerator.getExitCode());
        assertTrue(output.getOut().contains("Unable to load command"));
    }
}
