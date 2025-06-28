package org.heyner.excelutils.directoryparser;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.heyner.excelutils.TestInitializerFactory;
import org.heyner.excelutils.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.formatactivity.FormatActivity;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class DirectoryParserTest {
    @Mock
    private CorrectionImputation correctionImputation;
    @Mock
    private FormatActivity formatActivity;
    private final String fileName1 = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";
    private final String fileName2 = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-UC_PCB_PROJ_TRX_03_1265199083.xlsx";
    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }
    @Test
    void testDirectoryParser() throws IOException {
        DirectoryParser d1 = new DirectoryParser(correctionImputation, formatActivity);
        doNothing().when(correctionImputation).execute(any(),any());
        doNothing().when(formatActivity).execute(any());

        d1.execute("directory_parser", TestInitializerFactory.getPathTest()+"/");
        assertFalse(d1.isListFilesEmpty());

        assertTrue(Files.exists(Paths.get(fileName1)));
        assertTrue(Files.exists(Paths.get(fileName2)));

    }
    @Test
    @ExpectSystemExitWithStatus(0)
    void testDirectoryParser2() throws IOException {
        new File("target/empty").mkdir();
        DirectoryParser d1 = new DirectoryParser(correctionImputation, formatActivity);
        d1.execute("directory_parser", "target/empty/");
        assertTrue(d1.isListFilesEmpty());
    }
}