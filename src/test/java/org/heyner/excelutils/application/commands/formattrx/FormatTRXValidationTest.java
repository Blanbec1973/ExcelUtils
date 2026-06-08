package org.heyner.excelutils.application.commands.formattrx;

import org.heyner.excelutils.application.ports.FormatTRXPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class FormatTRXValidationTest {

    private FormatTRX formatTRX;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        formatTRX = new FormatTRX(mock(FormatTRXPort.class), null);
    }

    @Test
    void validate_throws_when_input_file_path_is_null() {
        String[] args = {"formattrx", null};
        assertThrows(IllegalArgumentException.class, () -> formatTRX.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_path_is_blank() {
        String[] args = {"formattrx", "  "};
        assertThrows(IllegalArgumentException.class, () -> formatTRX.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_does_not_exist() {
        String[] args = {"formattrx", "/nonexistent/file.xlsx"};
        assertThrows(IllegalArgumentException.class, () -> formatTRX.parse(args),
                "Input file does not exist");
    }

    @Test
    void validate_throws_when_input_path_is_directory() throws Exception {
        Path dir = tempDir.resolve("testdir");
        Files.createDirectory(dir);
        String[] args = {"formattrx", dir.toString()};
        assertThrows(IllegalArgumentException.class, () -> formatTRX.parse(args),
                "Input path is not a file");
    }

    @Test
    void validate_succeeds_with_valid_file() throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"formattrx", file.toString()};
        FormatTRXArgs result = formatTRX.parse(args);
        assertNotNull(result);
    }
}


