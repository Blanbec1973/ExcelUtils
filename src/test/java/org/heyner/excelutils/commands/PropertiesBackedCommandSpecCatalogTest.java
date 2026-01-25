package org.heyner.excelutils.commands;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class PropertiesBackedCommandSpecCatalogTest {
//TODO régler tests
//    private final ApplicationContextRunner contextRunner =
//            new ApplicationContextRunner()
//                    .withConfiguration(AutoConfigurations.of(ConfigurationPropertiesAutoConfiguration.class))
//                    .withPropertyValues(
//                            // <-- tes propriétés de test
//                            "spring.profiles.active=catalog-it",
//                            "command.commands.directoryparser.counterarguments=2",
//                            "command.commands.fusiontrx.counterarguments=2",
//                            "logging.level.org.springframework.boot.context.properties=DEBUG",
//                            "logging.level.org.springframework.boot.context.properties.bind=TRACE"
//
//                    );
//
//
//    @Test
//    void shouldBuildCatalogFromProperties() {
//        contextRunner.run(ctx -> {
//            assertThat(ctx).hasSingleBean(PropertiesBackedCommandSpecCatalog.class);
//            var catalog = ctx.getBean(PropertiesBackedCommandSpecCatalog.class);
//
//            assertThat(catalog.names())
//                    .containsAll(Set.of("directoryparser", "fusiontrx"));
//
//            assertThat(catalog.find("DIRECTORYPARSER"))
//                    .isPresent()
//                    .get()
//                    .extracting(CommandSpec::expectedArgs)
//                    .isEqualTo(2);
//        });
//    }
//
//    @Test
//    void shouldFailFastOnInvalidConfig() {
//        new ApplicationContextRunner()
//                .withConfiguration(AutoConfigurations.of(ConfigurationPropertiesAutoConfiguration.class))
//                .withPropertyValues(
//                        "spring.profiles.active=catalog-it",
//                        "command.commands.directoryparser.counterarguments=0" // invalide: < 1
//                )
//                .run(ctx -> {
//                    // Contexte démarre mais bean 'catalog' échoue à l'instanciation
//                    assertThat(ctx).hasFailed();
//                    assertThat(ctx.getStartupFailure())
//                            .hasRootCauseInstanceOf(IllegalArgumentException.class)
//                            .hasMessageContaining("expectedArgs must be >= 1");
//                });
//    }
}
