package org.heyner.excelutils.directoryparser.processors;

import java.io.File;
import java.io.IOException;

public interface FileProcessor {
    /** Indique si ce processeur doit agir sur ce fichier. */
    boolean supports(File file);

    /** Traite le fichier (peut lancer IOException). */
    void process(File file) throws IOException;
}

