package org.heyner.excelutils.application.commands.lissage;

import org.heyner.excelutils.application.ports.LissagePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class LissageValidationTest {

    private Lissage lissage;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        lissage = new Lissage(mock(LissagePort.class));
    }

    @Test
    void validate_throws_when_input_file_path_is_null() {
        String[] args = {"lissage", null, "2", "0.25"};
        assertThrows(IllegalArgumentException.class, () -> lissage.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_path_is_blank() {
        String[] args = {"lissage", "  ", "2", "0.25"};
        assertThrows(IllegalArgumentException.class, () -> lissage.parse(args),
                "Input file path must not be blank");
    }

    @Test
    void validate_throws_when_input_file_does_not_exist() {
        String[] args = {"lissage", "/nonexistent/file.xlsx", "2", "0.25"};
        assertThrows(IllegalArgumentException.class, () -> lissage.parse(args),
                "Input file does not exist");
    }

    @Test
    void validate_throws_when_input_path_is_directory() throws Exception {
        Path dir = tempDir.resolve("testdir");
        Files.createDirectory(dir);
        String[] args = {"lissage", dir.toString(), "2", "0.25"};
        assertThrows(IllegalArgumentException.class, () -> lissage.parse(args),
                "Input path is not a file");
    }

    static Stream<Arguments> invalidLissageRowArgs() {
        return Stream.of(
                Arguments.of("abc",  "0.25"),   // row non numérique
                Arguments.of("0",    "0.25"),   // row < 1
                Arguments.of("-1",   "0.25"),   // row négatif
                Arguments.of("2",    "abc"),    // marge non numérique
                Arguments.of("2",    "0"),      // marge = 0
                Arguments.of("2",    "1"),      // marge = 1
                Arguments.of("2",    "1.5"),    // marge > 1
                Arguments.of("2",    "-0.1")    // marge négative
        );
    }

    @ParameterizedTest(name = "row={0}, margin={1} doit lever une IllegalArgumentException")
    @MethodSource("invalidLissageRowArgs")
    void validate_throws_for_invalid_row_or_margin(String row, String margin) throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"lissage", file.toString(), row, margin};
        assertThrows(IllegalArgumentException.class, () -> lissage.parse(args));
    }

    @ParameterizedTest(name = "margin={0} doit être acceptée")
    @ValueSource(strings = {"0.25", "0,75", "0.1", "0.99"})
    void validate_succeeds_with_valid_parameters(String margin) throws Exception {
        Path file = tempDir.resolve("test.xlsx");
        Files.createFile(file);
        String[] args = {"lissage", file.toString(), "2", margin};
        LissageArgs result = lissage.parse(args);
        assertNotNull(result);
    }
}

