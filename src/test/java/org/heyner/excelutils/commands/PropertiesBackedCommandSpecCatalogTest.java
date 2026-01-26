package org.heyner.excelutils.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesBackedCommandSpecCatalogTest {

    @Test
    void builds_catalog_from_flat_properties() {
        CommandProperties props = new CommandProperties();
        props.getCounterarguments().put("AnalyzeTRX", 2);
        props.getCounterarguments().put("fusiontrx", 2);

        PropertiesBackedCommandSpecCatalog catalog =
                new PropertiesBackedCommandSpecCatalog(props);

        assertThat(catalog.find("ANALYZETRX"))
                .get()
                .extracting(CommandSpec::expectedArgs)
                .isEqualTo(2);
    }






}
