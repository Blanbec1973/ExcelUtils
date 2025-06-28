package org.heyner.excelutils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CommandProperties.class)
@TestPropertySource("classpath:application.properties")
@EnableConfigurationProperties(CommandProperties.class)
class CommandPropertiesTest {

    @Autowired
    private CommandProperties commandProperties;

    @Test
    void testCommandConfigsAreLoaded() {
        Map<String, CommandProperties.CommandConfig> commands = commandProperties.getCommands();

        assertFalse(commands.isEmpty(), "La map de commandes ne doit pas être vide");

        assertTrue(commands.containsKey("analyzetrx"), "La commande 'analyze-trx' doit être présente");
        assertEquals(2, commands.get("analyzetrx").getCounterarguments(), "Le nombre d'arguments doit être 2");

        assertTrue(commands.containsKey("fusiontrx"), "La commande 'fusion-trx' doit être présente");
        assertEquals(2, commands.get("fusiontrx").getCounterarguments(), "Le nombre d'arguments doit être 2");
    }


    @Test
    void contextLoads() {
        assertNotNull(commandProperties, "Le bean CommandProperties n'est pas injecté !");
    }

}
