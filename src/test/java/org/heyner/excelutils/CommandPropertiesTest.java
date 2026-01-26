package org.heyner.excelutils;

import org.heyner.excelutils.commands.CommandProperties;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLnConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CommandProperties.class)
@EnableConfigurationProperties(CommandProperties.class)
class CommandPropertiesTest {

    @Autowired
    @Qualifier("commandProperties")
    private CommandProperties commandProperties;

    @Test
    void testCommandConfigsAreLoaded() {
        Map<String, Integer> commands = commandProperties.getCounterarguments();
        System.out.println("Map : "+ commands);

        assertFalse(commands.isEmpty(), "La map de commandes ne doit pas être vide");

        assertTrue(commands.containsKey("analyzetrx"), "La commande 'analyze-trx' doit être présente");
        assertEquals(2, commands.get("analyzetrx"), "Le nombre d'arguments doit être 2");

        assertTrue(commands.containsKey("fusiontrx"), "La commande 'fusion-trx' doit être présente");
        assertEquals(2, commands.get("fusiontrx"), "Le nombre d'arguments doit être 2");
    }


    @Test
    void contextLoads() {
        assertNotNull(commandProperties, "Le bean CommandProperties n'est pas injecté !");
    }

}
