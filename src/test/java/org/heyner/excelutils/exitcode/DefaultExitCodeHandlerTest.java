package org.heyner.excelutils.exitcode;

import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.cli.CommandHelpCatalog;
import org.heyner.excelutils.cli.CommandHelpEntry;
import org.heyner.excelutils.shared.constants.CustomExitCodeGenerator;
import org.heyner.excelutils.shared.constants.DefaultExitCodeHandler;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultExitCodeHandlerTest {
    private CliPrinter cliPrinter;
    private CommandHelpCatalog helpCatalog;
    private CustomExitCodeGenerator gen;
    private DefaultExitCodeHandler handler;

    @BeforeEach
    void setUp() {
        cliPrinter = mock(CliPrinter.class);
        helpCatalog = mock(CommandHelpCatalog.class);
        gen = new CustomExitCodeGenerator();
        handler = new DefaultExitCodeHandler(gen, helpCatalog, cliPrinter);
    }

    @Test
    void shouldHandleGracefulExit() {
        handler.handle(new GracefulExitException("ok", 0));

        assertEquals(0, gen.getExitCode());
    }

    @Test
    void gracefulExitPrintsSuccessMessage() {
        handler.handle(new GracefulExitException("operation done", 0));

        verify(cliPrinter).info("SUCCESS - operation done");
    }

    @Test
    void shouldHandleFunctionalException() {
        FunctionalException fe = new FunctionalException("functional", 7) {};
        handler.handle(fe);

        assertEquals(7, gen.getExitCode());
    }

    @Test
    void shouldHandleFatalException() {
        handler.handle(new FatalApplicationException("fatal", null, 9));

        assertEquals(9, gen.getExitCode());
    }

    @Test
    void fatalExceptionPrintsResourceInErrorMessage() {
        handler.handle(new FatalApplicationException("myFile.xlsx", null, 9));

        verify(cliPrinter).error("""
                        ERROR: Fatal error while processing myFile.xlsx
                        """);
    }

    @Test
    void shouldHandleUnexpectedException() {
        handler.handle(new RuntimeException("boom"));

        assertEquals(1, gen.getExitCode());
    }

    @Test
    void unexpectedExceptionPrintsGenericErrorMessage() {
        handler.handle(new RuntimeException("boom"));

        verify(cliPrinter).error("ERROR: unexpected error");
    }

    @Test
    void catalogConfigurationExceptionSetsExitCode() {
        handler.handle(new CatalogConfigurationException("bad config", ExitCodes.CONFIG_ERROR));

        assertEquals(ExitCodes.CONFIG_ERROR, gen.getExitCode());
    }

    @Test
    void catalogConfigurationExceptionDoesNotPrintToCliPrinter() {
        handler.handle(new CatalogConfigurationException("bad config", ExitCodes.CONFIG_ERROR));

        verifyNoInteractions(cliPrinter);
    }

    @Test
    void missingConfigurationExceptionSetsExitCode() {
        handler.handle(new MissingConfigurationException(ExitCodes.USAGE_ERROR));

        assertEquals(ExitCodes.USAGE_ERROR, gen.getExitCode());
    }

    @Test
    void missingConfigurationExceptionPrintsErrorMessage() {
        handler.handle(new MissingConfigurationException(ExitCodes.USAGE_ERROR));

        verify(cliPrinter).error("""
            ERROR: missing command
            Usage: excelutils help
            """);
    }

    @Test
    void invalidFunctionExceptionSetsExitCode() {
        handler.handle(new InvalidFunctionException("badcmd", ExitCodes.USAGE_ERROR));

        assertEquals(ExitCodes.USAGE_ERROR, gen.getExitCode());
    }

    @Test
    void invalidFunctionExceptionPrintsFunctionName() {
        handler.handle(new InvalidFunctionException("badcmd", ExitCodes.USAGE_ERROR));

        verify(cliPrinter).info("""
                ERROR: unknown command: badcmd
                Usage: excelutils help""");
    }

    @Test
    void invalidArgumentCountExceptionSetsExitCode() {
        when(helpCatalog.find("analyzetrx")).thenReturn(Optional.empty());

        handler.handle(new InvalidArgumentCountException("analyzetrx", 2, 1, ExitCodes.USAGE_ERROR));

        assertEquals(ExitCodes.USAGE_ERROR, gen.getExitCode());
    }

    @Test
    void invalidArgumentCountExceptionPrintsUsageFromCatalog() {
        when(helpCatalog.find("analyzetrx")).thenReturn(Optional.of(
                new CommandHelpEntry("analyzetrx", "desc", "analyzetrx <file>", "example")));

        handler.handle(new InvalidArgumentCountException("analyzetrx", 2, 1, ExitCodes.USAGE_ERROR));

        verify(cliPrinter).error("""
            ERROR: invalid number of arguments
            Command: analyzetrx
            Expected: 2
            Received: 1
            Usage: analyzetrx <file>
            """);
    }

    @Test
    void invalidArgumentCountExceptionFallsBackToHelpWhenCommandNotInCatalog() {
        when(helpCatalog.find("unknown")).thenReturn(Optional.empty());

        handler.handle(new InvalidArgumentCountException("unknown", 1, 0, ExitCodes.USAGE_ERROR));

        verify(cliPrinter).error("""
            ERROR: invalid number of arguments
            Command: unknown
            Expected: 1
            Received: 0
            Usage: excelutils help
            """);
    }
}
