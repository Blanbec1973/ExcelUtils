package org.heyner.excelutils.application.commands.directoryparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DirectoryParserValidationTest {

    private DirectoryParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new DirectoryParser(Collections.emptyList(), mock(DirectoryLister.class), mock(FileClassifier.class));
    }

    @Test
    void validate_throws_when_directory_path_is_null() {
        String[] args = {"directoryparser", null};
        assertThrows(IllegalArgumentException.class, () -> parser.parse(args),
                "Directory path must not be blank");
    }

    @Test
    void validate_throws_when_directory_path_is_blank() {
        String[] args = {"directoryparser", "  "};
        assertThrows(IllegalArgumentException.class, () -> parser.parse(args),
                "Directory path must not be blank");
    }

    @Test
    void validate_throws_when_directory_does_not_exist() {
        String[] args = {"directoryparser", "/nonexistent/directory"};
        assertThrows(IllegalArgumentException.class, () -> parser.parse(args),
                "Directory does not exist");
    }

    @Test
    void validate_throws_when_path_is_file() throws Exception {
        Path file = tempDir.resolve("test.txt");
        Files.createFile(file);
        String[] args = {"directoryparser", file.toString()};
        assertThrows(IllegalArgumentException.class, () -> parser.parse(args),
                "Path is not a directory");
    }

    @Test
    void validate_succeeds_with_valid_directory() {
        String[] args = {"directoryparser", tempDir.toString()};
        DirectoryParserArgs result = parser.parse(args);
        assertNotNull(result);
    }
}



