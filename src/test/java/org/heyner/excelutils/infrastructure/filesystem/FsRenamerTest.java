package org.heyner.excelutils.infrastructure.filesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FsRenamerTest {

    private final FsRenamer fsRenamer = new FsRenamer();

    @TempDir
    Path tempDir;

    @Test
    void renameMovesExistingFileToTargetPath() throws IOException {
        Path input = Files.writeString(tempDir.resolve("input.txt"), "content");
        Path output = tempDir.resolve("output.txt");

        fsRenamer.rename(input.toString(), output.toString());

        assertFalse(Files.exists(input));
        assertTrue(Files.exists(output));
        assertEquals("content", Files.readString(output));
    }

    @Test
    void renameDoesNotThrowWhenInputFileDoesNotExist() {
        Path input = tempDir.resolve("missing.txt");
        Path output = tempDir.resolve("output.txt");

        assertDoesNotThrow(() -> fsRenamer.rename(input.toString(), output.toString()));

        assertFalse(Files.exists(input));
        assertFalse(Files.exists(output));
    }
}


