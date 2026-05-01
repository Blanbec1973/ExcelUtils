package org.heyner.excelutils.directoryparser.processors;

import org.heyner.excelutils.directoryparser.FileClassifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

abstract class AbstractFileProcessorContractTest<P extends FileProcessor> {

    private final FileClassifier classifier = new FileClassifier();

    /** Doit retourner l'instance à tester */
    protected abstract P newProcessor();

    /** Fichiers qui DOIVENT matcher getSupportedFileType() */
    protected abstract List<Path> matchingSamples();

    /** Fichiers qui NE DOIVENT PAS matcher getSupportedFileType() */
    protected abstract List<Path> nonMatchingSamples();

    @Test
    void supports_contract() {
        P p = newProcessor();

        for (Path f : matchingSamples()) {
            assertEquals(p.getSupportedFileType(), classifier.classify(f), () -> "Expected type match for: " + f.toString());
        }
        for (Path f : nonMatchingSamples()) {
            assertNotEquals(p.getSupportedFileType(), classifier.classify(f), () -> "Expected type not match for: " + f.toString());
        }
    }
}
