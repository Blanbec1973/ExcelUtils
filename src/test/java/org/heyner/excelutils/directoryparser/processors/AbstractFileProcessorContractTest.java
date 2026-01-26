
package org.heyner.excelutils.directoryparser.processors;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractFileProcessorContractTest<P extends FileProcessor> {

    /** Doit retourner l'instance à tester */
    protected abstract P newProcessor();

    /** Fichiers qui DOIVENT matcher supports() */
    protected abstract List<Path> matchingSamples();

    /** Fichiers qui NE DOIVENT PAS matcher supports() */
    protected abstract List<Path> nonMatchingSamples();

    @Test
    void supports_contract() {
        P p = newProcessor();

        for (Path f : matchingSamples()) {
            assertTrue(p.supports(f), () -> "Expected supports=true for: " + f.toString());
        }
        for (Path f : nonMatchingSamples()) {
            assertFalse(p.supports(f), () -> "Expected supports=false for: " + f.toString());
        }
    }
}
