package org.heyner.excelutils.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HelpServiceTest {
    private Slf4jCliPrinter cliPrinter;
    private CommandHelpCatalog catalog;
    private HelpPrinter helpPrinter;

    @BeforeEach
    void setUp() {
        cliPrinter = mock(Slf4jCliPrinter.class);
        catalog = mock(CommandHelpCatalog.class);
        helpPrinter = new HelpPrinter(cliPrinter, catalog);
    }

    @Test
    void printAll_prints_header_and_all_entries() {
        CommandHelpEntry first = new CommandHelpEntry("analyzetrx", "desc1", "usage1", "example1");
        CommandHelpEntry second = new CommandHelpEntry("lissage", "desc2", "usage2", "example2");
        when(catalog.all()).thenReturn(List.of(first, second));

        helpPrinter.printAll();

        var order = inOrder(cliPrinter);
        order.verify(cliPrinter).blankLine();

        order.verify(cliPrinter).info("analyzetrx");
        order.verify(cliPrinter).info("  desc1");
        order.verify(cliPrinter).info("  Usage: usage1");
        order.verify(cliPrinter).info("  Example: example1");
        order.verify(cliPrinter).blankLine();

        order.verify(cliPrinter).info("lissage");
        order.verify(cliPrinter).info("  desc2");
        order.verify(cliPrinter).info("  Usage: usage2");
        order.verify(cliPrinter).info("  Example: example2");
        order.verify(cliPrinter).blankLine();
    }

    @Test
    void printCommand_prints_entry_when_command_exists() {
        CommandHelpEntry entry = new CommandHelpEntry("fusiontrx", "desc", "usage", "example");
        when(catalog.find("fusiontrx")).thenReturn(java.util.Optional.of(entry));

        helpPrinter.printCommand("fusiontrx");

        verify(cliPrinter).info("fusiontrx");
        verify(cliPrinter).info("  desc");
        verify(cliPrinter).info("  Usage: usage");
        verify(cliPrinter).info("  Example: example");
        verify(cliPrinter).blankLine();
    }

    @Test
    void printCommand_prints_error_when_command_is_unknown() {
        when(catalog.find("unknown")).thenReturn(java.util.Optional.empty());

        helpPrinter.printCommand("unknown");

        verify(cliPrinter).error("""
            ERROR: unknown command: unknown
            Usage: excelutils help
            """);
    }
}

