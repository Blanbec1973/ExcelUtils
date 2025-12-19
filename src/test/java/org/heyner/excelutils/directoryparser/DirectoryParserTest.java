package org.heyner.excelutils.directoryparser;

import org.heyner.excelutils.exceptions.FileHandlingException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.TestInitializerFactory;
import org.heyner.excelutils.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.format_trx.FormatTRX;
import org.heyner.excelutils.formatactivity.FormatActivity;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class DirectoryParserTest {
    @Mock
    private CorrectionImputation correctionImputation;
    @Mock
    private FormatActivity formatActivity;
    @Mock
    private FormatInvRegisterLN formatInvRegisterLN;
    @Mock
    private FormatTRX formatTRX;
    private final String fileName1 = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";
    private final String fileName2 = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx";
    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }
    @Test
    void testDirectoryParser() throws IOException {
        DirectoryParser d1 = new DirectoryParser(correctionImputation, formatActivity, formatInvRegisterLN, formatTRX);
        doNothing().when(correctionImputation).execute(any(),any());
        doNothing().when(formatActivity).execute(any());

        d1.execute("directory_parser", TestInitializerFactory.getPathTest()+"/");
        assertFalse(d1.isListFilesEmpty());

        assertTrue(Files.exists(Paths.get(fileName1)));
        assertTrue(Files.exists(Paths.get(fileName2)));

        // Vérifications d'appels
        verify(formatActivity,   times(1)).execute(any());
        verify(correctionImputation, times(1)).execute(any(), any());
        verify(formatTRX,        times(1)).execute(any());

    }
    @Test
    void testDirectoryParser2() {
        File dir = new File("target/empty");
        if (!dir.exists() && !dir.mkdir()) {
            fail("Impossible de créer le dossier : " + dir.getAbsolutePath());
        }

        DirectoryParser d1 = new DirectoryParser(correctionImputation, formatActivity, formatInvRegisterLN, formatTRX);
        assertThrows(GracefulExitException.class,
                () -> d1.execute("directory_parser", "target/empty/")
        );
    }


    @Test
    void shouldFailFastOnFirstHandlerError() throws IOException {
        DirectoryParser d1 = new DirectoryParser(correctionImputation, formatActivity, formatInvRegisterLN, formatTRX);

        // Simule une IOException dans le handler activity
        doThrow(new IOException("boom")).when(formatActivity).execute(any());

        String pathTest = TestInitializerFactory.getPathTest() + "/";
        assertThrows(FileHandlingException.class, () ->
                d1.execute("directory_parser", pathTest)
        );
    }

}