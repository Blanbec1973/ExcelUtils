package org.heyner.excelutils.application.commands.formatinvregisterln;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class FormatInvRegisterLNValidationTest {

    private FormatInvRegisterLN formatInvLN;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        formatInvLN = new FormatInvRegisterLN(mock(org.heyner.excelutils.infrastructure.config.FormatInvRegisterLnConfig.class));
    }

    @Test
    void validate_throws_when_input_file_path_is_null() {
        String[] args = {"formatinvregisterln", null};
        assertThrows(IllegalArgumentException.class, () -> formatInvLN.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_path_is_blank() {
        String[] args = {"formatinvregisterln", "  "};
        assertThrows(IllegalArgumentException.class, () -> formatInvLN.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_does_not_exist() {
        String[] args = {"formatinvregisterln", "/nonexistent/file.xlsx"};
        assertThrows(IllegalArgumentException.class, () -> formatInvLN.parse(args),
                "Input file does not exist");
    }

    @Test
    void validate_throws_when_input_path_is_directory() throws Exception {
        Path dir = tempDir.resolve("testdir");
        Files.createDirectory(dir);
        String[] args = {"formatinvregisterln", dir.toString()};
        assertThrows(IllegalArgumentException.class, () -> formatInvLN.parse(args),
                "Input path is not a file");
    }

    @Test
    void validate_succeeds_with_valid_file() throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"formatinvregisterln", file.toString()};
        FormatInvRegisterLNArgs result = formatInvLN.parse(args);
        assertNotNull(result);
    }
}

