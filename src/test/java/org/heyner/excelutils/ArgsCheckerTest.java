package org.heyner.excelutils;

import org.heyner.excelutils.commands.CommandSpec;
import org.heyner.excelutils.commands.CommandSpecCatalog;
import org.heyner.excelutils.exceptions.InvalidArgumentCountException;
import org.heyner.excelutils.exceptions.InvalidFunctionException;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArgsCheckerTest {

    @Test
    void ok_whenCommandExists_andArgsCountMatches() {
        // given
        CommandSpecCatalog catalog = mock(CommandSpecCatalog.class);
        when(catalog.find("directoryparser"))
                .thenReturn(Optional.of(new CommandSpec("directoryparser", 2)));

        ArgsChecker checker = new ArgsChecker(catalog);

        // when / then
        assertDoesNotThrow(() -> checker.validateOrThrow(new String[]{"directoryparser", "/tmp/dir"}));
        // (optionnel) vérifier qu'on n’a pas fait d'autres appels
        verify(catalog).find("directoryparser");
        verifyNoMoreInteractions(catalog);
    }

    @Test
    void fail_whenNoArgs() {
        CommandSpecCatalog catalog = mock(CommandSpecCatalog.class);
        ArgsChecker checker = new ArgsChecker(catalog);

        assertThrows(MissingConfigurationException.class, () -> checker.validateOrThrow(new String[] {}));
        verifyNoInteractions(catalog); // rien n’est appelé si args est vide
    }

    @Test
    void fail_whenUnknownCommand() {
        CommandSpecCatalog catalog = mock(CommandSpecCatalog.class);
        when(catalog.find("unknown")).thenReturn(Optional.empty());

        ArgsChecker checker = new ArgsChecker(catalog);

        assertThrows(InvalidFunctionException.class, () -> checker.validateOrThrow(new String[]{"unknown"}));
        verify(catalog).find("unknown");
        verifyNoMoreInteractions(catalog);
    }

    @Test
    void fail_whenWrongArgsCount() {
        CommandSpecCatalog catalog = mock(CommandSpecCatalog.class);
        when(catalog.find("fusiontrx"))
                .thenReturn(Optional.of(new CommandSpec("fusiontrx", 2)));

        ArgsChecker checker = new ArgsChecker(catalog);

        assertThrows(InvalidArgumentCountException.class, () -> checker.validateOrThrow(new String[]{"fusiontrx"}));
        verify(catalog).find("fusiontrx");
        verifyNoMoreInteractions(catalog);
    }


    @Test
    void lookup_is_case_insensitive_if_checker_normalizes() {
        CommandSpecCatalog catalog = mock(CommandSpecCatalog.class);
        // Le checker normalise en lower, mais on accepte n’importe quelle casse ici :
        when(catalog.find(argThat(cmd -> cmd != null && cmd.equalsIgnoreCase("DIRECTORYPARSER"))))
                .thenReturn(Optional.of(new CommandSpec("directoryparser", 2)));

        ArgsChecker checker = new ArgsChecker(catalog);

        assertDoesNotThrow(() -> checker.validateOrThrow(new String[]{"DIRECTORYPARSER", "/tmp"}));
        // Interaction réelle se fera avec "directoryparser"
        verify(catalog).find(argThat(cmd -> cmd.equals("directoryparser")));
        verifyNoMoreInteractions(catalog);
    }

}
