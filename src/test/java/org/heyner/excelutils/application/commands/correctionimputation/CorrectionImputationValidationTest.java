package org.heyner.excelutils.application.commands.correctionimputation;

import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.heyner.excelutils.infrastructure.config.CorrectionImputationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class CorrectionImputationValidationTest {

    private CorrectionImputation correctionImputation;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        correctionImputation = new CorrectionImputation(
                mock(CorrectionImputationConfig.class),
                mock(CorrectionImputationPort.class)
        );
    }

    @Test
    void validate_throws_when_input_file_path_is_null() {
        String[] args = {"correctionimputation", null};
        assertThrows(IllegalArgumentException.class, () -> correctionImputation.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_path_is_blank() {
        String[] args = {"correctionimputation", "  "};
        assertThrows(IllegalArgumentException.class, () -> correctionImputation.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_does_not_exist() {
        String[] args = {"correctionimputation", "/nonexistent/file.xlsx"};
        assertThrows(IllegalArgumentException.class, () -> correctionImputation.parse(args),
                "Input file does not exist");
    }

    @Test
    void validate_throws_when_input_path_is_directory() throws Exception {
        Path dir = tempDir.resolve("testdir");
        Files.createDirectory(dir);
        String[] args = {"correctionimputation", dir.toString()};
        assertThrows(IllegalArgumentException.class, () -> correctionImputation.parse(args),
                "Input path is not a file");
    }

    @Test
    void validate_succeeds_with_valid_file() throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"correctionimputation", file.toString()};
        CorrectionImputationArgs result = correctionImputation.parse(args);
        assertNotNull(result);
    }
}

