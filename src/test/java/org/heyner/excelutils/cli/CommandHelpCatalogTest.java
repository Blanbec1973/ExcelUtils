package org.heyner.excelutils.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandHelpCatalogTest {

    private final CommandHelpCatalog catalog = new CommandHelpCatalog();

    @Test
    void allReturnsNonEmptyList() {
        List<CommandHelpEntry> entries = catalog.all();

        assertFalse(entries.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"analyzetrx", "fusiontrx", "lissage"})
    void allContainsExpectedCommand(String commandName) {
        List<CommandHelpEntry> entries = catalog.all();

        assertTrue(entries.stream().anyMatch(e -> e.name().equals(commandName)));
    }

    @Test
    void allEntriesHaveNonBlankFields() {
        for (CommandHelpEntry entry : catalog.all()) {
            assertFalse(entry.name().isBlank(), "name should not be blank");
            assertFalse(entry.description().isBlank(), "description should not be blank");
            assertFalse(entry.usage().isBlank(), "usage should not be blank");
            assertFalse(entry.example().isBlank(), "example should not be blank");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "analyzetrx, analyzetrx",
            "ANALYZETRX, analyzetrx",
            "FusionTrx,  fusiontrx"
    })
    void findReturnsMatchingEntryRegardlessOfCase(String input, String expectedName) {
        Optional<CommandHelpEntry> result = catalog.find(input);

        assertTrue(result.isPresent());
        assertEquals(expectedName, result.get().name());
    }

    @Test
    void findReturnsEmptyForUnknownCommand() {
        Optional<CommandHelpEntry> result = catalog.find("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void findReturnsEmptyForEmptyString() {
        Optional<CommandHelpEntry> result = catalog.find("");

        assertTrue(result.isEmpty());
    }
}

