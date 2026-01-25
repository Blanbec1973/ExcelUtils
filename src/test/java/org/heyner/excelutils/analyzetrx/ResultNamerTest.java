package org.heyner.excelutils.analyzetrx;

import org.heyner.excelutils.utils.ExcelPrefixReader;
import org.heyner.excelutils.utils.FsRenamePort;
import org.heyner.excelutils.utils.FsRenamer;
import org.heyner.excelutils.utils.PrefixReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        String inputName = "toto.com";
        when(prefixReader.read(inputName, "dummy", "dummy")).thenReturn("300000000073657");

        resultNamer.renameIfNeeded(inputName, "dummy", "dummy");

        verify(fsRenamer, times(1)).rename(inputName, "300000000073657-toto.com");
    }

    @Test
    void renameIfNeededNotPassedTest() {
        String inputName = "300000000073657-toto.com";
        verify(prefixReader, times(0)).read(any(), any(), any());
        verify(fsRenamer, times(0)).rename(any(), any());
    }

    @Test
    void hasFileNumericPrefixTest() {
        assertFalse(resultNamer.hasFileNumericPrefix("toto.com"));
        assertTrue(resultNamer.hasFileNumericPrefix("300000000073657-toto.com"));
    }
}