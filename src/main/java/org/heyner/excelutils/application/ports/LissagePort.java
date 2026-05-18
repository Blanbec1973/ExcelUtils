package org.heyner.excelutils.application.ports;

import java.nio.file.Path;

public interface LissagePort {
    void smooth(Path inputFile, int lissageRow, double targetMargin);
}

