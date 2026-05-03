package org.heyner.excelutils.directoryparser;

import org.heyner.excelutils.TestInitializerFactory;
import org.heyner.excelutils.application.commands.directoryparser.*;
import org.heyner.excelutils.application.commands.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.exceptions.FileProcessorException;
import org.heyner.excelutils.shared.exceptions.GracefulExitException;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class DirectoryParserTest {
    @Mock
    private FileProcessor activityRenameProcessor;
    @Mock
    private FileClassifier classifier;
    private final DirectoryLister lister = new DirectoryLister();
    private final Path pathTest = Path.of("target/temp-"+this.getClass().getSimpleName());

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }


    @Test
    void shouldCallActivityRenameProcessor() throws IOException {
        // --- Arrange
        // 1) getSupportedFileType() -> ACTIVITY
        when(activityRenameProcessor.getSupportedFileType()).thenReturn(FileType.ACTIVITY);

        // 2) classifier returns ACTIVITY for activity files, otherwise UNKNOWN
        when(classifier.classify(argThat(f ->
                f != null && f.toString().contains(ExcelConstants.ACTIVITY_SHEET))))
                .thenReturn(FileType.ACTIVITY);
        when(classifier.classify(argThat(f ->
                f != null && !f.toString().contains(ExcelConstants.ACTIVITY_SHEET))))
                .thenReturn(FileType.UNKNOWN);

        // 3) process(..) ne fait rien (on vérifie juste l'appel)
        doNothing().when(activityRenameProcessor).process(argThat(f ->
                f != null && f.toString().contains(ExcelConstants.ACTIVITY_SHEET)));

        DirectoryParser parser = new DirectoryParser(
                List.of(activityRenameProcessor),
                lister,
                classifier
        );

        // Counting files in directory :
        long expectedActivityCount =
                Arrays.stream(Objects.requireNonNull(pathTest.toFile().listFiles()))
                        .filter(f -> f.toString().endsWith(".xlsx"))
                        .filter(f -> f.toString().contains(ExcelConstants.ACTIVITY_SHEET))
                        .count();

        // --- Act
        parser.execute(new DirectoryParserArgs(pathTest));

        // --- Assert
        verify(activityRenameProcessor, times((int) expectedActivityCount))
                .process(argThat(f -> f.toString().contains(ExcelConstants.ACTIVITY_SHEET)));
        verifyNoMoreInteractions(activityRenameProcessor);
    }

    @Test
    void shouldCallGracefulExitWhenEmptyDirectory() {
        File dir = new File("target/empty");
        if (!dir.exists() && !dir.mkdir()) {
            fail("Unable to create the folder : " + dir.getAbsolutePath());
        }

        DirectoryParser d1 = new DirectoryParser(List.of(), lister, classifier);
        Path pathEmpty = Path.of("target/empty/");
        DirectoryParserArgs args = new DirectoryParserArgs(pathEmpty);
        assertThrows(GracefulExitException.class,
                () -> d1.execute(args)
        );
    }

    @Test
    void shouldFailFastOnFirstProcessorError() throws IOException {
        // Arrange
        FileProcessor failing = mock(FileProcessor.class);
        when(failing.getSupportedFileType()).thenReturn(FileType.TRX);
        when(classifier.classify(any())).thenReturn(FileType.TRX);
        doThrow(new IOException("boom")).when(failing).process(any()); // fails on the very first file

        DirectoryParser d1 = new DirectoryParser(List.of(failing), lister, classifier);
        DirectoryParserArgs args = new DirectoryParserArgs(pathTest);

        // Act + Assert
        assertThrows(FileProcessorException.class, () ->
                d1.execute(args));
    }


}