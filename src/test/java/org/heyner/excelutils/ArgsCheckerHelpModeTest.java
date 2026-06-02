package org.heyner.excelutils;

import org.heyner.excelutils.application.commands.core.CommandSpecCatalog;
import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class ArgsCheckerHelpModeTest {

    @ParameterizedTest
    @ValueSource(strings = {"help", "--help", "HELP", "--HELP"})
    void validateOrThrow_returns_true_and_skips_catalog_for_help_mode(String command) {
        CommandSpecCatalog catalog = mock(CommandSpecCatalog.class);
        ArgsChecker checker = new ArgsChecker(catalog);

        boolean result = checker.validateOrThrow(new String[]{command});

        assertTrue(result);
        verifyNoInteractions(catalog);
    }
}

