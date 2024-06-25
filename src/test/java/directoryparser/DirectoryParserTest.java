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
    private TestInitializerFactory tif = new TestInitializerFactory(this.getClass().getSimpleName());
    private String fileName1 = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";
    private String fileName2 = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    DirectoryParserTest() throws IOException {
    }

    @Test
    void testDirectoryParser() throws IOException {
        DirectoryParser d1 = new DirectoryParser(tif.getPathTest()+"/");
        assertFalse(d1.isListFilesEmpty());
        d1.processList();

        assertTrue(Files.exists(Paths.get(fileName1)));
        assertTrue(Files.exists(Paths.get(fileName2)));

    }
    @Test
    void testDirectoryParser2() throws IOException {
        new File("target/tempvide").mkdir();
        DirectoryParser d1 = new DirectoryParser("target/tempvide/");
        assertTrue(d1.isListFilesEmpty());
    }
}