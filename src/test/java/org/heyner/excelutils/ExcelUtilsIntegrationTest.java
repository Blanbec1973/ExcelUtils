package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.heyner.excelutils.bootstrap.ExcelUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {ExcelUtils.class, ExcelUtilsIntegrationTest.TestConfig.class},
        args = {"servicetest", "arg1", "arg2"}
)
@Slf4j
@ExtendWith(OutputCaptureExtension.class)
class ExcelUtilsIntegrationTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ArgsChecker argsChecker() {
            return new ArgsChecker(null) {
                @Override
                public boolean validateOrThrow(String[] args) {
                    return true;
                }
            };
        }

        @Bean
        public Command<CommandArgs> serviceTest() {
            return new Command<>() {
                @Override
                public String name() {
                    return "servicetest";
                }
                @Override
                public CommandTestArgs parse(String[] args) {
                    return new CommandTestArgs("servicetest");
                }

                @Override
                public void execute(CommandArgs args) {
                    log.info("Test command executed with args: {}", args);
                }
            };
        }
    }

    @Test
    void testMainRunsSuccessfully(CapturedOutput output) {
        assertThat(output.getOut()).contains("Beginning");
        assertThat(output.getOut()).contains("servicetest");
    }
}


record CommandTestArgs(String value) implements CommandArgs {}