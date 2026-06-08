package org.heyner.excelutils.application.commands.analyzetrx;

import org.heyner.excelutils.application.ports.ExcelTransferPort;
import org.heyner.excelutils.cli.Slf4jCliPrinter;
import org.heyner.excelutils.infrastructure.config.AnalyzeTRXConfig;
import org.heyner.excelutils.shared.util.DateTemplateExpander;
import org.heyner.excelutils.shared.util.filenaming.ResultNamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AnalyzeTRXValidationTest {

    private AnalyzeTRX analyzeTRX;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        analyzeTRX = new AnalyzeTRX(
            mock(AnalyzeTRXConfig.class),
            mock(DateTemplateExpander.class),
            mock(ModelCloner.class),
            mock(ExcelTransferPort.class),
            mock(ResultNamer.class),
            mock(Slf4jCliPrinter.class)
        );
    }

    @Test
    void validate_throws_when_input_file_path_is_null() {
        String[] args = {"analyzetrx", null};
        assertThrows(IllegalArgumentException.class, () -> analyzeTRX.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_path_is_blank() {
        String[] args = {"analyzetrx", "  "};
        assertThrows(IllegalArgumentException.class, () -> analyzeTRX.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_does_not_exist() {
        String[] args = {"analyzetrx", "/nonexistent/file.xlsx"};
        assertThrows(IllegalArgumentException.class, () -> analyzeTRX.parse(args),
                "Input file does not exist");
    }

    @Test
    void validate_throws_when_input_path_is_directory() throws Exception {
        Path dir = tempDir.resolve("testdir");
        Files.createDirectory(dir);
        String[] args = {"analyzetrx", dir.toString()};
        assertThrows(IllegalArgumentException.class, () -> analyzeTRX.parse(args),
                "Input path is not a file");
    }

    @Test
    void validate_succeeds_with_valid_file() throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"analyzetrx", file.toString()};
        AnalyzeTRXArgs result = analyzeTRX.parse(args);
        assertNotNull(result);
    }
}

