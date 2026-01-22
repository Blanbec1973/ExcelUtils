package org.heyner.excelutils.commands;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesBackedCommandSpecCatalogIT {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(TestConfig.class)
                    .withPropertyValues(
                            // <-- tes propriétés de test
                            "app.commands.directoryparser=2",
                            "app.commands.fusiontrx=2"
                    );

    @Configuration
    @EnableConfigurationProperties(CommandProperties.class)
    static class TestConfig {
        @Bean
        PropertiesBackedCommandSpecCatalog catalog(CommandProperties props) {
            return new PropertiesBackedCommandSpecCatalog(props);
        }
    }

    @Test
    void shouldBuildCatalogFromProperties() {
        contextRunner.run(ctx -> {
            assertThat(ctx).hasSingleBean(PropertiesBackedCommandSpecCatalog.class);
            var catalog = ctx.getBean(PropertiesBackedCommandSpecCatalog.class);

            assertThat(catalog.names())
                    .containsAll(Set.of("directoryparser", "fusiontrx"));

            assertThat(catalog.find("DIRECTORYPARSER"))
                    .isPresent()
                    .get()
                    .extracting(CommandSpec::expectedArgs)
                    .isEqualTo(2);
        });
    }

    @Test
    void shouldFailFastOnInvalidConfig() {
        new ApplicationContextRunner()
                .withUserConfiguration(TestConfig.class)
                .withPropertyValues(
                        "app.commands.directoryparser=0" // invalide: < 1
                )
                .run(ctx -> {
                    // Contexte démarre mais bean 'catalog' échoue à l'instanciation
                    assertThat(ctx).hasFailed();
                    assertThat(ctx.getStartupFailure())
                            .hasRootCauseInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("expectedArgs must be >= 1");
                });
    }
}
