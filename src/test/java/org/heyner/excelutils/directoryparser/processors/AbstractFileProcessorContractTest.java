
package org.heyner.excelutils.directoryparser.processors;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractFileProcessorContractTest<P extends FileProcessor> {

    /** Doit retourner l'instance à tester */
    protected abstract P newProcessor();

    /** Fichiers qui DOIVENT matcher supports() */
    protected abstract List<File> matchingSamples();

    /** Fichiers qui NE DOIVENT PAS matcher supports() */
    protected abstract List<File> nonMatchingSamples();

    @Test
    void supports_contract() {
        P p = newProcessor();

        for (File f : matchingSamples()) {
            assertTrue(p.supports(f), () -> "Expected supports=true for: " + f.getName());
        }
        for (File f : nonMatchingSamples()) {
            assertFalse(p.supports(f), () -> "Expected supports=false for: " + f.getName());
        }
    }
}
