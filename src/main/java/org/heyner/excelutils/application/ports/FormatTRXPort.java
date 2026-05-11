package org.heyner.excelutils.application.ports;

import java.nio.file.Path;

public interface FormatTRXPort {
    void deleteFirstLineContaining(Path inputFile, String sheet, String header);
}
