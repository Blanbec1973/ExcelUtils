
package org.heyner.excelutils;

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

        CommandRegistry registry = new CommandRegistry(List.of(service));
        dispatcher = new CommandDispatcher(props, argsChecker, registry, exitHandler);
    }

    @Test
    void shouldExecuteCommandWhenValid() throws Exception {
        String[] args = {"test", "arg1"};

        when(argsChecker.validate(args)).thenReturn(true);
        doNothing().when(service).execute(args);

        dispatcher.run(args);

        verify(service).execute(args);
        verifyNoInteractions(exitHandler);
    }

    @Test
    void shouldDelegateToExitHandlerOnGracefulExit() throws Exception {
        String[] args = {"test"};

        when(argsChecker.validate(args)).thenReturn(true);
        doThrow(new GracefulExitException("bye", 0))
                .when(service).execute(args);

        dispatcher.run(args);

        verify(exitHandler).handle(any(GracefulExitException.class));
    }
}
