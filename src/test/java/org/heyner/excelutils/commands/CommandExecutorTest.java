package org.heyner.excelutils.commands;

import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandExecutor;
import org.heyner.excelutils.application.commands.core.CommandRegistry;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.MissingConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    private CommandRegistry registry;

    @InjectMocks
    private CommandExecutor sut;

    @Test
    void execute_happy_path_parses_and_executes_command_in_order() throws Exception {
        // Arrange
        String[] args = {"analyzetrx", "input.xlsx"};
        Command command = mock(Command.class);
        CommandArgs parsed = mock(CommandArgs.class);

        when(registry.find("analyzetrx")).thenReturn(command);
        when(command.parse(args)).thenReturn(parsed);

        // Act
        sut.execute(args);

        // Assert – vérifier l'ordre des appels
        InOrder inOrder = inOrder(registry, command);
        inOrder.verify(registry).find("analyzetrx");
        inOrder.verify(command).parse(args);
        inOrder.verify(command).execute(parsed);
        verifyNoMoreInteractions(registry, command);
    }

    @Test
    void execute_throws_MissingConfigurationException_when_command_not_found() {
        // Arrange
        String[] args = {"unknowncommand"};
        when(registry.find("unknowncommand")).thenReturn(null);

        // Act & Assert
        MissingConfigurationException exception = assertThrows(
                MissingConfigurationException.class,
                () -> sut.execute(args)
        );

        // Vérifier que le message et le code sont corrects
        assert exception.getExitCode() == ExitCodes.MISSING_CONFIGURATION;
        assert exception.getMessage().contains("unknowncommand");

        verify(registry).find("unknowncommand");
    }

}