package org.heyner.excelutils.directoryparser;

import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.TestInitializerFactory;
import org.heyner.excelutils.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.exceptions.FileProcessorException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class DirectoryParserTest {
    @Mock
    private FileProcessor activityRenameProcessor;
    @Mock
    private FormatInvRegisterLN formatInvRegisterLN;
    private final DirectoryLister lister = new DirectoryLister();
    private final String pathTest = "target/temp-"+this.getClass().getSimpleName();

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }


    @Test
    void shouldCallActivityRenameProcessor() throws IOException {
        // --- Arrange
        // 1) supports(..) -> true UNIQUEMENT pour les fichiers "activity"
        when(activityRenameProcessor.supports(argThat(f ->
                f != null && f.toString().contains(ExcelConstants.ACTIVITY_SHEET)))) // "AR_ITEM_ACTIVITY"
                .thenReturn(true);
        when(activityRenameProcessor.supports(argThat(f ->
                f != null && !f.toString().contains(ExcelConstants.ACTIVITY_SHEET)))) // "AR_ITEM_ACTIVITY"
                .thenReturn(false);

        // 2) process(..) ne fait rien (on vérifie juste l'appel)
        doNothing().when(activityRenameProcessor).process(argThat(f ->
                f != null && f.toString().contains(ExcelConstants.ACTIVITY_SHEET)));

        DirectoryParser parser = new DirectoryParser(
                List.of(activityRenameProcessor),
                lister
        );

        // 3) Compter les fichiers "activity" dans le dossier
        long expectedActivityCount =
                Arrays.stream(Path.of(pathTest).toFile().listFiles())
                        .filter(f -> f.toString().endsWith(".xlsx"))
                        .filter(f -> f.toString().contains(ExcelConstants.ACTIVITY_SHEET))
                        .count();

        // --- Act
        parser.execute("directoryparser", pathTest);

        // --- Assert
        verify(activityRenameProcessor, times((int) expectedActivityCount))
                .process(argThat(f -> f.toString().contains(ExcelConstants.ACTIVITY_SHEET)));
        verifyNoMoreInteractions(activityRenameProcessor);
    }

    @Test
    void shouldCallGracefulExitWhenEmptyDirectory() {
        File dir = new File("target/empty");
        if (!dir.exists() && !dir.mkdir()) {
            fail("Impossible de créer le dossier : " + dir.getAbsolutePath());
        }

        DirectoryParser d1 = new DirectoryParser(List.of(), lister);
        assertThrows(GracefulExitException.class,
                () -> d1.execute("directory_parser", "target/empty/")
        );
    }

    @Test
    void shouldFailFastOnFirstProcessorError() throws IOException {
        // Arrange
        FileProcessor failing = mock(FileProcessor.class);
        when(failing.supports(any())).thenReturn(true);               // il "supporte" tout
        doThrow(new IOException("boom")).when(failing).process(any()); // il échoue dès le premier fichier

        DirectoryParser d1 = new DirectoryParser(List.of(failing), lister);

        // Act + Assert
        assertThrows(FileProcessorException.class, () ->
                d1.execute("directory_parser", pathTest));
    }


}