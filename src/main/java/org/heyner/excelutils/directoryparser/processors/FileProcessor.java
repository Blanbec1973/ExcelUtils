package org.heyner.excelutils.directoryparser.processors;

import java.io.IOException;
import java.nio.file.Path;

public interface FileProcessor {
    /** Indique si ce processeur doit agir sur ce fichier. */
    boolean supports(Path path);

    /** Traite le fichier (peut lancer IOException). */
    void process(Path path) throws IOException;
}

