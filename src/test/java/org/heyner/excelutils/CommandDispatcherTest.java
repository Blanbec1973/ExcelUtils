
package org.heyner.excelutils;

import org.heyner.excelutils.exceptions.GracefulExitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandDispatcherTest {

    private ArgsChecker argsChecker;
    private CustomExitCodeGenerator exitCodeGenerator;
    private CommandService commandService;
    private CommandDispatcher dispatcher;
    private CommandRegistry registry;

    @BeforeEach
    void setUp() {
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        argsChecker = mock(ArgsChecker.class);
        exitCodeGenerator = new CustomExitCodeGenerator();
        commandService = mock(CommandService.class);
        registry = mock(CommandRegistry.class);

        when(applicationProperties.getProjectName()).thenReturn("ExcelUtils");
        when(applicationProperties.getVersion()).thenReturn("1.0.0");
        when(commandService.getCommandName()).thenReturn("test");

        dispatcher = new CommandDispatcher(
                applicationProperties,
                List.of(commandService),
                argsChecker,
                registry, exitCodeGenerator
        );
    }

    @Test
    void shouldExecuteCommandWhenValid() throws Exception {
        String[] args = {"test", "arg1"};

        when(argsChecker.validate(args)).thenReturn(true);
        when(registry.find(any())).thenReturn(Optional.ofNullable(commandService));
        doNothing().when(commandService).execute(args);

        dispatcher.run(args);

        verify(commandService).execute(args);
        assertEquals(0, exitCodeGenerator.getExitCode());
    }

    @Test
    void shouldSetExitCodeWhenCommandNotFound() {
        String[] args = {"unknown"};

        when(argsChecker.validate(args)).thenReturn(true);
        when(registry.find(any())).thenReturn(Optional.empty());

        dispatcher.run(args);

        assertEquals(2, exitCodeGenerator.getExitCode());
    }

    @Test
    void shouldHandleGracefulExitException() throws Exception {
        String[] args = {"test"};

        when(argsChecker.validate(args)).thenReturn(true);
        when(registry.find(any())).thenReturn(Optional.ofNullable(commandService));
        doThrow(new GracefulExitException("End", 5))
                .when(commandService).execute(args);

        dispatcher.run(args);

        assertEquals(5, exitCodeGenerator.getExitCode());
    }
}
