package org.heyner.excelutils.commands;

import org.heyner.excelutils.*;
import org.heyner.excelutils.commands.commandParser.CommandParser;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.exitcode.ExitCodeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class CommandDispatcherTest {

    private ArgsChecker argsChecker;
    private ExitCodeHandler exitHandler;
    private CommandService service;
    private CommandParser parser;
    private CommandDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        ApplicationProperties props = mock(ApplicationProperties.class);
        when(props.getProjectName()).thenReturn("ExcelUtils");
        when(props.getVersion()).thenReturn("1.0.0");

        argsChecker = mock(ArgsChecker.class);
        exitHandler = mock(ExitCodeHandler.class);
        service = mock(CommandService.class);
        when(service.getCommandName()).thenReturn("test");
        parser = mock(CommandParser.class);

        CommandRegistry registry = new CommandRegistry(List.of(service));
        dispatcher = new CommandDispatcher(props, argsChecker, registry, exitHandler, parser);
    }

    @Test
    void shouldExecuteCommandWhenValid() throws Exception {
        String[] args = {"test", "arg1"};
        CommandArgs commandArgs = mock(CommandArgs.class);

        when(argsChecker.validateOrThrow(args)).thenReturn(true);
        when(parser.parse(args)).thenReturn(commandArgs);
        doNothing().when(service).execute(commandArgs);

        dispatcher.run(args);

        verify(service).execute(commandArgs);
        verifyNoInteractions(exitHandler);
    }

    @Test
    void shouldDelegateToExitHandlerOnGracefulExit() throws Exception {
        String[] args = {"test"};
        CommandArgs commandArgs = mock(CommandArgs.class);

        when(argsChecker.validateOrThrow(args)).thenReturn(true);
        when(parser.parse(args)).thenReturn(commandArgs);
        doThrow(new GracefulExitException("bye", 0))
                .when(service).execute(commandArgs);

        dispatcher.run(args);

        verify(exitHandler).handle(any(GracefulExitException.class));
    }
}
