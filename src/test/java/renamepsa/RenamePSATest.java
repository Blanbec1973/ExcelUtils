package renamepsa;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitPreventedException;
import commun.TestInitializerFactory;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RenamePSATest {
    private final String fileName1 = "target\\temp-"+this.getClass().getSimpleName()+"\\UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";
    private final String fileName2 = "target\\temp-"+this.getClass().getSimpleName()+"\\UC_PCB_PROJ_TRX_03_1265199083.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }

    @Test @Order(1)
    void testRenamePSA() throws IOException {
        File dossier = new File(TestInitializerFactory.getPathTest() + "/");
        File [] files =  dossier.listFiles();
        assert files != null;
        assertEquals(6,files.length);
        assertEquals(fileName1, files[4].toString());
        assertEquals(fileName2, files[5].toString());

        RenamePSA.action(new String[]{TestInitializerFactory.getPathTest()+"/", "UC_PCB_PROJ_TRX", "sheet1", "B3"});
        RenamePSA.action(new String[]{TestInitializerFactory.getPathTest()+"/", "UC_AR_ITEM_ACTIVITY", "sheet1", "G3"});
        files = dossier.listFiles();
        assert files != null;
        assertEquals("target\\temp-RenamePSATest\\300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx",
                              files[0].toString());
        assertEquals("target\\temp-RenamePSATest\\300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx",
                              files[1].toString());
    }

    @Test @Order(2)
    @ExpectSystemExitWithStatus(0)
    void testMainDossierVide() {
        new File("target/empty").mkdir();

        Assertions.assertThrows(SystemExitPreventedException.class, () -> RenamePSA.action(new String[]{"target/empty", "UC_PCB_PROJ_TRX", "sheet1", "B3"}));
        new File("target/empty").delete();
    }


}