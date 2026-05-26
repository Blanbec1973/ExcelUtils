package org.heyner.excelutils.application.commands.fusiontrx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class FusionTRXValidationTest {

    private FusionTRX fusionTRX;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fusionTRX = new FusionTRX(mock(FusionProcessor.class));
    }

    // --- Tests paramétrés sur le répertoire d'entrée ---

    static Stream<Arguments> invalidInputDirArgs() {
        return Stream.of(
                Arguments.of((Object) null),        // null
                Arguments.of("  "),                 // blank
                Arguments.of("/nonexistent/dir")    // inexistant
        );
    }

    @ParameterizedTest(name = "inputDir=''{0}'' doit lever une IllegalArgumentException")
    @MethodSource("invalidInputDirArgs")
    void validate_throws_for_invalid_input_directory(String inputDir) {
        String[] args = {"fusiontrx", inputDir, "/output"};
        assertThrows(IllegalArgumentException.class, () -> fusionTRX.parse(args));
    }

    @Test
    void validate_throws_when_input_path_is_file() throws Exception {
        Path file = tempDir.resolve("test.txt");
        Files.createFile(file);
        String[] args = {"fusiontrx", file.toString(), tempDir.toString()};
        assertThrows(IllegalArgumentException.class, () -> fusionTRX.parse(args),
                "Input path is not a directory");
    }

    // --- Tests paramétrés sur le répertoire de sortie ---

    static Stream<Arguments> invalidOutputDirArgs() {
        return Stream.of(
                Arguments.of((Object) null),        // null
                Arguments.of("  "),                 // blank
                Arguments.of("/nonexistent/dir")    // inexistant
        );
    }

    @ParameterizedTest(name = "outputDir=''{0}'' doit lever une IllegalArgumentException")
    @MethodSource("invalidOutputDirArgs")
    void validate_throws_for_invalid_output_directory(String outputDir) throws Exception {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectory(inputDir);
        String[] args = {"fusiontrx", inputDir.toString(), outputDir};
        assertThrows(IllegalArgumentException.class, () -> fusionTRX.parse(args));
    }

    @Test
    void validate_throws_when_output_path_is_file() throws Exception {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectory(inputDir);
        Path outputFile = tempDir.resolve("output.txt");
        Files.createFile(outputFile);
        String[] args = {"fusiontrx", inputDir.toString(), outputFile.toString()};
        assertThrows(IllegalArgumentException.class, () -> fusionTRX.parse(args),
                "Output path is not a directory");
    }

    // --- Cas de succès ---

    @Test
    void validate_succeeds_with_valid_directories() throws Exception {
        Path inputDir = tempDir.resolve("input");
        Path outputDir = tempDir.resolve("output");
        Files.createDirectory(inputDir);
        Files.createDirectory(outputDir);
        String[] args = {"fusiontrx", inputDir.toString(), outputDir.toString()};
        FusionTRXArgs result = fusionTRX.parse(args);
        assertNotNull(result);
    }
}
