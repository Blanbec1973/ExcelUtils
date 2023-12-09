package directoryParser;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryParserTest {

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("target/temp");
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File("target/temp"));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"), new File("target/temp/"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDirectoryParser() throws IOException {
        DirectoryParser d1 = new DirectoryParser("target/temp/");
        assertFalse(d1.isListFilesEmpty());
        d1.processList();

        assertTrue(Files.exists(Paths.get("target/temp/300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx")));
        assertTrue(Files.exists(Paths.get("target/temp/300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx")));

    }
    @Test
    void testDirectoryParser2() throws IOException {
        FileUtils.cleanDirectory(new File("target/temp"));
        DirectoryParser d1 = new DirectoryParser("target/temp/");
        assertTrue(d1.isListFilesEmpty());
    }
}