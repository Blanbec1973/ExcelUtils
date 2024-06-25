package renamepsa;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitPreventedException;
import commun.TestInitializerFactory;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RenamePSATest {
    private final TestInitializerFactory tif = new TestInitializerFactory(this.getClass().getSimpleName());
    private final String fileName1 = "target\\temp-"+this.getClass().getSimpleName()+"\\UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";
    private final String fileName2 = "target\\temp-"+this.getClass().getSimpleName()+"\\UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    RenamePSATest() throws IOException {
    }

    @Test @Order(1)
    void testRenamePSA() throws IOException {
        File dossier = new File(tif.getPathTest() + "/");
        File [] files =  dossier.listFiles();
        assertEquals(5,files.length);
        assertEquals(fileName1, files[3].toString());
        assertEquals(fileName2, files[4].toString());

        RenamePSA renamePSA = new RenamePSA();
        renamePSA.renamePSA(new String[]{tif.getPathTest()+"/", "UC_PCB_PROJ_TRX", "sheet1", "B3"});
        renamePSA.renamePSA(new String[]{tif.getPathTest()+"/", "UC_AR_ITEM_ACTIVITY", "sheet1", "G3"});
        files = dossier.listFiles();
        assertEquals("target\\temp-RenamePSATest\\300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx",
                              files[0].toString());
        assertEquals("target\\temp-RenamePSATest\\300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx",
                              files[1].toString());
    }

    @Test @Order(2)
    @ExpectSystemExitWithStatus(0)
    void testMainDossierVide() throws IOException {
        new File("target/temp/dossierVide").mkdir();

        SystemExitPreventedException thrown = Assertions.assertThrows(SystemExitPreventedException.class, () -> {
            RenamePSA.main(new String[]{"target/temp/dossierVide", "UC_PCB_PROJ_TRX", "sheet1", "B3"});
        });
        new File("target/temp/dossierVide").delete();
    }


}