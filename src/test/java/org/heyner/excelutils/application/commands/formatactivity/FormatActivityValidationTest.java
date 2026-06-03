package org.heyner.excelutils.application.commands.formatactivity;

import org.heyner.excelutils.application.ports.FormatActivityPort;
import org.heyner.excelutils.cli.Slf4jCliPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class FormatActivityValidationTest {

    private FormatActivity formatActivity;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        formatActivity = new FormatActivity(mock(FormatActivityPort.class),
                mock(Slf4jCliPrinter.class));
    }

    @Test
    void validate_throws_when_input_file_path_is_null() {
        String[] args = {"formatactivity", null};
        assertThrows(IllegalArgumentException.class, () -> formatActivity.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_path_is_blank() {
        String[] args = {"formatactivity", "  "};
        assertThrows(IllegalArgumentException.class, () -> formatActivity.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_does_not_exist() {
        String[] args = {"formatactivity", "/nonexistent/file.xlsx"};
        assertThrows(IllegalArgumentException.class, () -> formatActivity.parse(args),
                "Input file does not exist");
    }

    @Test
    void validate_throws_when_input_path_is_directory() throws Exception {
        Path dir = tempDir.resolve("testdir");
        Files.createDirectory(dir);
        String[] args = {"formatactivity", dir.toString()};
        assertThrows(IllegalArgumentException.class, () -> formatActivity.parse(args),
                "Input path is not a file");
    }

    @Test
    void validate_succeeds_with_valid_file() throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"formatactivity", file.toString()};
        FormatActivityArgs result = formatActivity.parse(args);
        assertNotNull(result);
    }
}



