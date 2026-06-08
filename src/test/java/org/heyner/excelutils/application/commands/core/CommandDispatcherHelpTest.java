package org.heyner.excelutils.application.commands.core;

import org.heyner.excelutils.application.service.ArgsChecker;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.cli.HelpPrinter;
import org.heyner.excelutils.shared.config.ApplicationProperties;
import org.heyner.excelutils.shared.exitcode.ExitCodeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class CommandDispatcherHelpTest {

    private ArgsChecker argsChecker;
    private ExitCodeHandler exitHandler;
    private CommandDispatcher dispatcher;
    private CommandExecutor commandExecutor;
    private HelpPrinter helpPrinter;

    @BeforeEach
    void setUp() {
        ApplicationProperties props = mock(ApplicationProperties.class);
        argsChecker = mock(ArgsChecker.class);
        exitHandler = mock(ExitCodeHandler.class);
        commandExecutor = mock(CommandExecutor.class);
        helpPrinter = mock(HelpPrinter.class);
        CliPrinter cliPrinter = mock(CliPrinter.class);

        dispatcher = new CommandDispatcher(props, argsChecker, exitHandler, commandExecutor, helpPrinter, cliPrinter);
    }

    @Test
    void run_prints_help_and_skips_validation_for_help_command() {
        dispatcher.run("help");

        verify(helpPrinter).printAll();
        verifyNoInteractions(argsChecker);
        verifyNoInteractions(commandExecutor);
        verifyNoInteractions(exitHandler);
    }

    @Test
    void run_prints_help_and_skips_validation_for_long_help_command() {
        dispatcher.run("--help");

        verify(helpPrinter).printAll();
        verifyNoInteractions(argsChecker);
        verifyNoInteractions(commandExecutor);
        verifyNoInteractions(exitHandler);
    }
}

