package org.heyner.excelutils.utils.filenaming;

import org.heyner.excelutils.utils.filenaming.FsRenamePort;
import org.heyner.excelutils.utils.PrefixReader;
import org.heyner.excelutils.utils.filenaming.ResultNamer;
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

        resultNamer.renameIfNeeded(inputName, "dummy", "dummy");
        verify(prefixReader, times(0)).read(any(), any(), any());
        verify(fsRenamer, times(0)).rename(any(), any());
    }

    @Test
    void hasFileNumericPrefixTest() {
        assertFalse(resultNamer.hasFileNumericPrefix("toto.com"));
        assertTrue(resultNamer.hasFileNumericPrefix("300000000073657-toto.com"));
    }
}