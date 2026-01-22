package org.heyner.excelutils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {ExcelUtils.class, ServiceTest.class, ApplicationProperties.class},
        args = {"servicetest", "arg1", "arg2"}
)
@ExtendWith(OutputCaptureExtension.class)
class ExcelUtilsIntegrationTest {

    @TestConfiguration
    static class TestArgsCheckerConfig {
        @Bean
        public ArgsChecker argsChecker() {
            return new ArgsChecker(null) {
                @Override
                public boolean validateOrThrow(String[] args) {
                    return true;
                }
            };
        }
    }
    @Test
    void testMainRunsSuccessfully(CapturedOutput output) {
        // Vérifie que l'application a bien démarré et exécuté la commande
        assertThat(output.getOut()).contains("Beginning");
        assertThat(output.getOut()).contains("servicetest");
    }
}


