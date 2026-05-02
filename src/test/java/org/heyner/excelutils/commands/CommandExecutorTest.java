package org.heyner.excelutils.commands;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandExecutor;
import org.heyner.excelutils.application.commands.core.CommandRegistry;
import org.heyner.excelutils.application.commands.core.CommandService;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.application.commands.core.commandparser.CommandParser;
import org.heyner.excelutils.shared.exceptions.MissingConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    private CommandRegistry registry;

    @Mock
    private CommandParser parser;

    @Mock
    private CommandService service;

    @InjectMocks
    private CommandExecutor sut;

    @Test
    void execute_happy_path_parses_and_executes_command_in_order() throws Exception {
        // Arrange
        String[] args = {"analyzetrx", "input.xlsx"};
        CommandArgs commandArgs = mock(CommandArgs.class);

        when(registry.find("analyzetrx")).thenReturn(Optional.of(service));
        when(parser.parse(args)).thenReturn(commandArgs);
        doNothing().when(service).execute(commandArgs);

        // Act
        sut.execute(args);

        // Assert – vérifier l'ordre des appels
        InOrder inOrder = inOrder(registry, parser, service);
        inOrder.verify(registry).find("analyzetrx");
        inOrder.verify(parser).parse(args);
        inOrder.verify(service).execute(commandArgs);
        verifyNoMoreInteractions(registry, parser, service);
    }

    @Test
    void execute_throws_MissingConfigurationException_when_command_not_found() {
        // Arrange
        String[] args = {"unknowncommand", "arg1"};
        when(registry.find("unknowncommand")).thenReturn(Optional.empty());

        // Act & Assert
        MissingConfigurationException exception = assertThrows(
                MissingConfigurationException.class,
                () -> sut.execute(args)
        );

        // Vérifier que le message et le code sont corrects
        assert exception.getExitCode() == ExitCodes.MISSING_CONFIGURATION;
        assert exception.getMessage().contains("unknowncommand");

        // Vérifier que parser et service ne sont jamais appelés
        verify(registry).find("unknowncommand");
        verifyNoInteractions(parser, service);
    }

    @Test
    void execute_case_insensitive_command_lookup() throws Exception {
        // Arrange
        String[] args = {"ANALYZETRX", "input.xlsx"};
        CommandArgs commandArgs = mock(CommandArgs.class);

        when(registry.find("analyzetrx")).thenReturn(Optional.of(service));
        when(parser.parse(args)).thenReturn(commandArgs);
        doNothing().when(service).execute(commandArgs);

        // Act
        sut.execute(args);

        // Assert – vérifier que la commande est convertie en minuscules
        verify(registry).find("analyzetrx");
        verify(parser).parse(args);
        verify(service).execute(commandArgs);
    }

    @Test
    void execute_propagates_exception_from_service() throws Exception {
        // Arrange
        String[] args = {"analyzetrx", "input.xlsx"};
        CommandArgs commandArgs = mock(CommandArgs.class);
        IOException ioException = new IOException("File not found");

        when(registry.find("analyzetrx")).thenReturn(Optional.of(service));
        when(parser.parse(args)).thenReturn(commandArgs);
        doThrow(ioException).when(service).execute(commandArgs);

        // Act & Assert
        assertThrows(IOException.class, () -> sut.execute(args));

        // Vérifier que tous les appels ont été faits avant l'exception
        verify(registry).find("analyzetrx");
        verify(parser).parse(args);
        verify(service).execute(commandArgs);
    }
}

