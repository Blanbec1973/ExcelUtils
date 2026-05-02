package org.heyner.excelutils.application.commands.directoryparser;


import java.io.File;
import java.io.IOException;

@FunctionalInterface
interface FileHandler {
    boolean supports(File file);

    default void run(File file) {
        // action par défaut : rien
    }
}

