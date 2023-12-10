package renamepsa;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitPreventedException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RenamePSATest {

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("target/temp");
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File("target/temp"));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"), new File("target/temp/"));
    }

    @Test
    void testRenamePSA() throws IOException {
        File dossier = new File("target/temp/");
        File [] files =  dossier.listFiles();
        assertEquals(3,files.length);
        assertEquals("target\\temp\\UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx", files[1].toString());
        assertEquals("target\\temp\\UC_PCB_PROJ_TRX_03_1265199083.xlsx", files[2].toString());

        RenamePSA renamePSA = new RenamePSA();
        renamePSA.renamePSA(new String[]{"target/temp/", "UC_PCB_PROJ_TRX", "sheet1", "B3"});
        renamePSA.renamePSA(new String[]{"target/temp/", "UC_AR_ITEM_ACTIVITY", "sheet1", "G3"});
        files = dossier.listFiles();
        assertEquals("target\\temp\\300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx", files[0].toString());
        assertEquals("target\\temp\\300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx", files[1].toString());
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    void testMainDossierVide() throws IOException {
        new File("target/temp/dossierVide").mkdir();

        SystemExitPreventedException thrown = Assertions.assertThrows(SystemExitPreventedException.class, () -> {
            RenamePSA.main(new String[]{"target/temp/dossierVide", "UC_PCB_PROJ_TRX", "sheet1", "B3"});
        });
    }


}