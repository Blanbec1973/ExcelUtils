package org.heyner.excelutils.directoryparser;


import java.io.File;
import java.io.IOException;

@FunctionalInterface
interface FileHandler {
    boolean supports(File file);

    default void run(File file) throws IOException {
        // action par défaut : rien
    }
}

