package org.heyner.excelutils.directoryparser.processors;

import org.heyner.excelutils.directoryparser.FileType;

import java.io.IOException;
import java.nio.file.Path;

public interface FileProcessor {
    /** Indique si ce processeur doit agir sur ce fichier. */
    FileType getSupportedFileType();

    /** Traite le fichier (peut lancer IOException). */
    void process(Path path) throws IOException;
}

