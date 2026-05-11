package org.heyner.excelutils.application.ports;

import java.nio.file.Path;

public interface FormatActivityPort {
    void format(Path inputFile);
}