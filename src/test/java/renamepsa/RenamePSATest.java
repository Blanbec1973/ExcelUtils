package renamepsa;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitPreventedException;
import commun.TestInitializerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RenamePSATest {

    @BeforeEach
    void setUp() throws IOException {
        new TestInitializerFactory();
    }

    @Test
    void testRenamePSA() throws IOException {
        File dossier = new File("target/temp/");
        File [] files =  dossier.listFiles();
        assertEquals(4,files.length);
        assertEquals("target\\temp\\UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx", files[3].toString());
        assertEquals("target\\temp\\UC_PCB_PROJ_TRX_03_1265199083.xlsx", files[4].toString());

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