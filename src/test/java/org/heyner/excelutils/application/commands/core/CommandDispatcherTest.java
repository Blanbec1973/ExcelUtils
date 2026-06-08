package org.heyner.excelutils.application.commands.core;

import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.cli.HelpPrinter;
import org.heyner.excelutils.shared.config.ApplicationProperties;
import org.heyner.excelutils.shared.exitcode.ExitCodeHandler;
import org.heyner.excelutils.shared.exception.GracefulExitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommandDispatcherTest {

    private ArgsChecker argsChecker;
    private ExitCodeHandler exitHandler;
    private CommandDispatcher dispatcher;
    private CommandExecutor commandExecutor;
    private HelpPrinter helpPrinter;
    private CliPrinter cliPrinter;

    @BeforeEach
    void setUp()  {
        ApplicationProperties props = mock(ApplicationProperties.class);
        when(props.getProjectName()).thenReturn("ExcelUtils");
        when(props.getVersion()).thenReturn("1.0.0");

        argsChecker = mock(ArgsChecker.class);
        exitHandler = mock(ExitCodeHandler.class);
        helpPrinter = mock(HelpPrinter.class);
        cliPrinter = mock(CliPrinter.class);

        commandExecutor = mock(CommandExecutor.class);
        doNothing().when(commandExecutor).execute(any());
        dispatcher = new CommandDispatcher(props, argsChecker, exitHandler, commandExecutor, helpPrinter,  cliPrinter);
    }

    @Test
    void shouldExecuteCommandWhenValid() {
        String[] args = {"test", "arg1"};
        mock(CommandArgs.class);

        when(argsChecker.validateOrThrow(args)).thenReturn(true);

        dispatcher.run(args);

        verify(commandExecutor).execute(any());
        verifyNoInteractions(exitHandler);
    }

    @Test
    void shouldDelegateToExitHandlerOnGracefulExit() {
        String[] args = {"test"};
        mock(CommandArgs.class);

        doThrow(new GracefulExitException("bye", 0))
                .when(argsChecker).validateOrThrow(any());

        dispatcher.run(args);

        verify(exitHandler).handle(any(GracefulExitException.class));
    }
}
