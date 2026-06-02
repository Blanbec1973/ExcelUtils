package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.heyner.excelutils.bootstrap.ExcelUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {ExcelUtils.class, ExcelUtilsIntegrationTest.TestConfig.class},
        args = {"servicetest", "arg1", "arg2"}
)
@Slf4j
class ExcelUtilsIntegrationTest {

    private static final AtomicReference<String> EXECUTED_COMMAND = new AtomicReference<>();

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
                    EXECUTED_COMMAND.set(((CommandTestArgs) args).value());
                    log.info("Test command executed with args: {}", args);
                }
            };
        }
    }

    @Test
    void testMainRunsSuccessfully() {
        assertThat(EXECUTED_COMMAND.get()).isEqualTo("servicetest");
    }
}


record CommandTestArgs(String value) implements CommandArgs {}