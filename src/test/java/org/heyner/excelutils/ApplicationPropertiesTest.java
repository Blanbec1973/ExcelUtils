package org.heyner.excelutils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues(
                    "application.version=1.0.0",
                    "application.projectName=TestProject"
            );

    @Test
    void testPropertiesBinding() {
        contextRunner.run(context -> {
            ApplicationProperties props = context.getBean(ApplicationProperties.class);
            assertThat(props.getVersion()).isEqualTo("1.0.0");
            assertThat(props.getProjectName()).isEqualTo("TestProject");
        });
    }

    @EnableConfigurationProperties(ApplicationProperties.class)
    static class TestConfig {
    }
}
