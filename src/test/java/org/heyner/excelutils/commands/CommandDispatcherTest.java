package org.heyner.excelutils.commands;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandDispatcher;
import org.heyner.excelutils.application.commands.core.CommandExecutor;
import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.heyner.excelutils.shared.exceptions.GracefulExitException;
import org.heyner.excelutils.shared.constants.ExitCodeHandler;
import org.heyner.excelutils.shared.config.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommandDispatcherTest {

    private ArgsChecker argsChecker;
    private ExitCodeHandler exitHandler;
    private CommandDispatcher dispatcher;
    private CommandExecutor commandExecutor;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationProperties props = mock(ApplicationProperties.class);
        when(props.getProjectName()).thenReturn("ExcelUtils");
        when(props.getVersion()).thenReturn("1.0.0");

        argsChecker = mock(ArgsChecker.class);
        exitHandler = mock(ExitCodeHandler.class);

        commandExecutor = mock(CommandExecutor.class);
        doNothing().when(commandExecutor).execute(any());
        dispatcher = new CommandDispatcher(props, argsChecker, exitHandler, commandExecutor);
    }

    @Test
    void shouldExecuteCommandWhenValid() throws Exception {
        String[] args = {"test", "arg1"};
        CommandArgs commandArgs = mock(CommandArgs.class);

        when(argsChecker.validateOrThrow(args)).thenReturn(true);

        dispatcher.run(args);

        verify(commandExecutor).execute(any());
        verifyNoInteractions(exitHandler);
    }

    @Test
    void shouldDelegateToExitHandlerOnGracefulExit() {
        String[] args = {"test"};
        CommandArgs commandArgs = mock(CommandArgs.class);

        doThrow(new GracefulExitException("bye", 0))
                .when(argsChecker).validateOrThrow(any());

        dispatcher.run(args);

        verify(exitHandler).handle(any(GracefulExitException.class));
    }
}
