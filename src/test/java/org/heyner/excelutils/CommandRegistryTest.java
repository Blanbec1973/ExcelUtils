package org.heyner.excelutils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    private static class DummyService implements CommandService {
        private final String name;
        DummyService(String name) { this.name = name; }
        @Override public String getCommandName() { return name; }
        @Override public void execute(String... args) throws IOException { /* no-op */ }
    }

    @Test
    void shouldBuildRegistryAndFindByName_caseInsensitive() {
        CommandService s1 = new DummyService("formatactivity");
        CommandService s2 = new DummyService("fusiontrx");

        CommandRegistry registry = new CommandRegistry(List.of(s1, s2));

        assertTrue(registry.find("formatactivity").isPresent());
        assertTrue(registry.find("FORMATActivity").isPresent()); // case insensitive si tu lowercases
        assertTrue(registry.find("fusiontrx").isPresent());
        assertFalse(registry.find("unknown").isPresent());

        assertTrue(registry.getCommandNames().containsAll(List.of("formatactivity", "fusiontrx")));
    }

    @Test
    void shouldFailOnDuplicateCommand() {
        CommandService s1 = new DummyService("formatactivity");
        CommandService s2 = new DummyService("formatactivity");
        List<CommandService> myList = List.of(s1, s2);

        assertThrows(IllegalStateException.class, () -> new CommandRegistry(myList));
    }
}