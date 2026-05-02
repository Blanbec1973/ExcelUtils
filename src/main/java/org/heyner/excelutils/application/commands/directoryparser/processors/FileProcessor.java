package org.heyner.excelutils.application.commands.directoryparser.processors;

import org.heyner.excelutils.application.commands.directoryparser.FileType;

import java.io.IOException;
import java.nio.file.Path;

public interface FileProcessor {
    FileType getSupportedFileType();

    void process(Path path) throws IOException;
}

