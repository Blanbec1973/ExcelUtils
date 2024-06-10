package directoryparser;

import commun.TestInitializerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryParserTest {

    @BeforeAll
    static void beforeAll() throws IOException {
        new TestInitializerFactory();
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
        new File("target/tempvide").mkdir();
        DirectoryParser d1 = new DirectoryParser("target/tempvide/");
        assertTrue(d1.isListFilesEmpty());
    }
}