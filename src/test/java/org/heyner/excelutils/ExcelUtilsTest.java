
package org.heyner.excelutils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;



@SpringBootTest(
        classes = {ExcelUtils.class, ExcelUtilsTest.TestConfig.class},
        args = {"test", "arg1"}
)
class ExcelUtilsTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        CommandService testCommand() {
            return new CommandService() {
                @Override
                public String getCommandName() {
                    return "test";
                }

                @Override
                public void execute(String... args) {
                    // no-op
                }
            };
        }
    }

    @MockitoBean
    private ArgsChecker argsChecker;

    @MockitoBean
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        given(argsChecker.validateOrThrow(any())).willReturn(true);
        given(applicationProperties.getProjectName()).willReturn("ExcelUtils");
        given(applicationProperties.getVersion()).willReturn("Test");
    }

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}



