package org.heyner.excelutils.commands;

import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    private static class DummyCommand implements Command<CommandArgs> {
        private final String name;

        DummyCommand(String name) { this.name = name; }

        @Override
        public String name() { return name; }

        @Override
        public CommandArgs parse(String [] args) {
            return null;
        }

        @Override public void execute(CommandArgs args) {
            // do nothing
        }
    }

    @Test
    void should_find_command_by_name_case_insensitive() {
        Command<?> c1 = new DummyCommand("formatactivity");
        Command<?> c2 = new DummyCommand("fusiontrx");

        CommandRegistry registry = new CommandRegistry(List.of(c1, c2));

        assertNotNull(registry.find("formatactivity"));
        assertNotNull(registry.find("FORMATActivity"));
        assertNotNull(registry.find("fusiontrx"));
        assertEquals(Optional.empty(), registry.find("unknown"));
    }

    @Test
    void should_fail_on_duplicate_command() {
        Command<?> c1 = new DummyCommand("formatactivity");
        Command<?> c2 = new DummyCommand("formatactivity");
        List<Command<?>> commands = List.of(c1, c2);

        assertThrows(IllegalStateException.class,
                () -> new CommandRegistry(commands));
    }
}