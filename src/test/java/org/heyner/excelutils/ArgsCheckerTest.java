package org.heyner.excelutils;

import org.heyner.excelutils.exceptions.InvalidArgumentCountException;
import org.heyner.excelutils.exceptions.InvalidFunctionException;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ArgsCheckerTest {
    @Autowired
    private ArgsChecker argsChecker;
    @Mock
    private CommandProperties commandProperties;
    @BeforeEach
    void setUp() {
        argsChecker = new ArgsChecker(commandProperties);
    }
    @Test
    void shouldThrowExceptionWhenNoArguments() {
        MissingConfigurationException exception = assertThrows(
                MissingConfigurationException.class,
                () -> argsChecker.validate(new String[]{})
        );
        assertEquals("No argument, end of program.", exception.getMessage());
        assertEquals(-1, exception.getExitCode());
    }
    @Test
    void shouldThrowExceptionWhenFunctionIsInvalid() {
        String[] args = {"invalidFunction"};
        InvalidFunctionException exception = assertThrows(
                InvalidFunctionException.class,
                () -> argsChecker.validate(args)
        );
        assertTrue(exception.getMessage().contains("Invalid function"));
        assertEquals(-1, exception.getExitCode());
    }
    @Test
    void shouldThrowExceptionWhenNumberOfArgumentsIsInvalid() {
        String[] args = {"directoryparser", "arg1"};

        CommandProperties.CommandConfig mockConfig = mock(CommandProperties.CommandConfig.class);
        when(mockConfig.getCounterarguments()).thenReturn(3);

        // Créer une map contenant la fonction "validFunction"
        Map<String, CommandProperties.CommandConfig> mockMap = new HashMap<>();
        mockMap.put("directoryparser", mockConfig);

        // Configurer le mock de commandProperties
        when(commandProperties.getCommands()).thenReturn(mockMap);

        // Instancier ArgsChecker avec le mock
        ArgsChecker argsChecker2 = new ArgsChecker(commandProperties);

        InvalidArgumentCountException exception = assertThrows(
                InvalidArgumentCountException.class,
                () -> argsChecker2.validate(args)
        );
        System.out.println("Message : "+exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid number of arguments"));
        assertEquals(-1, exception.getExitCode());
    }
    @Test
    void shouldPassValidationWithValidArguments() {
        String[] args = {"directoryparser", "arg1", "arg2"};

        // Mock de la config de la commande
        CommandProperties.CommandConfig mockConfig = mock(CommandProperties.CommandConfig.class);
        when(mockConfig.getCounterarguments()).thenReturn(3);

        // Mock de la map des commandes
        Map<String, CommandProperties.CommandConfig> mockMap = Map.of("directoryparser", mockConfig);
        when(commandProperties.getCommands()).thenReturn(mockMap);

        // Configurer le mock de commandProperties
        when(commandProperties.getCommands()).thenReturn(mockMap);

        // Instancier ArgsChecker avec le mock
        ArgsChecker argsChecker3 = new ArgsChecker(commandProperties);

        // Appel réel
        boolean result = argsChecker3.validate(args);

        // Vérification
        assertTrue(result);
    }
}

