package org.heyner.excelutils.utils.filenaming;

import org.heyner.excelutils.infrastructure.filesystem.FsRenamePort;
import org.heyner.excelutils.shared.utils.PrefixReader;
import org.heyner.excelutils.shared.utils.filenaming.ResultNamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultNamerTest {
    @Mock
    private PrefixReader prefixReader;
    @Mock
    private FsRenamePort fsRenamer;
    private ResultNamer resultNamer;

    @BeforeEach
    void setUp() {
        resultNamer = new ResultNamer(prefixReader, fsRenamer);
    }

    @Test
    void renameIfNeededPassedTest() {
        Path inputName = Path.of("toto.com");
        when(prefixReader.read(String.valueOf(inputName), "dummy", "dummy")).thenReturn("300000000073657");

        resultNamer.renameIfNeeded(inputName, "dummy", "dummy");

        verify(fsRenamer, times(1)).rename(String.valueOf(inputName), "300000000073657-toto.com");
    }

    @Test
    void renameIfNeededNotPassedTest() {
        Path inputName = Path.of("300000000073657-toto.com");

        resultNamer.renameIfNeeded(inputName, "dummy", "dummy");
        verify(prefixReader, times(0)).read(any(), any(), any());
        verify(fsRenamer, times(0)).rename(any(), any());
    }

    @Test
    void hasFileNumericPrefixTest() {
        assertFalse(resultNamer.hasFileNumericPrefix("toto.com"));
        assertTrue(resultNamer.hasFileNumericPrefix("300000000073657-toto.com"));
    }

    @Test
    void fileWithExactly15DigitsHasNumericPrefix() {
        assertTrue(resultNamer.hasFileNumericPrefix("300000000073657"));
    }

    @Test
    void fileWithFewerThan15DigitsDoesNotHaveNumericPrefix() {
        assertFalse(resultNamer.hasFileNumericPrefix("30000000007365"));
    }

    @Test
    void emptyFileNameDoesNotHaveNumericPrefix() {
        assertFalse(resultNamer.hasFileNumericPrefix(""));
    }

    @Test
    void fileStartingWithLettersThenDigitsDoesNotHaveNumericPrefix() {
        assertFalse(resultNamer.hasFileNumericPrefix("abc300000000073657.xlsx"));
    }

    @Test
    void renameIfNeededWithParentDirectoryPreservesParentInTarget() {
        Path inputName = Path.of("some", "dir", "report.xlsx");
        when(prefixReader.read(String.valueOf(inputName), "Sheet1", "A1")).thenReturn("300000000073657");

        resultNamer.renameIfNeeded(inputName, "Sheet1", "A1");

        verify(fsRenamer, times(1)).rename(
                String.valueOf(inputName),
                Path.of("some", "dir", "300000000073657-report.xlsx").toString()
        );
    }

    @Test
    void renameIfNeededDoesNotReadPrefixWhenFileAlreadyHasNumericPrefix() {
        Path inputName = Path.of("some", "dir", "300000000073657-report.xlsx");

        resultNamer.renameIfNeeded(inputName, "Sheet1", "A1");

        verify(prefixReader, never()).read(any(), any(), any());
    }

    @Test
    void fileWith15DigitPrefixFollowedByTextHasNumericPrefix() {
        assertTrue(resultNamer.hasFileNumericPrefix("300000000073657report.xlsx"));
    }

    @Test
    void renameBuildsTargetNameWithPrefixDashFileName() {
        Path inputName = Path.of("activity.xlsx");
        when(prefixReader.read(String.valueOf(inputName), "Data", "B2")).thenReturn("123456789012345");

        resultNamer.renameIfNeeded(inputName, "Data", "B2");

        verify(fsRenamer).rename(String.valueOf(inputName), "123456789012345-activity.xlsx");
    }
}