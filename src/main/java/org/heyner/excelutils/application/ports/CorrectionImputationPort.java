package org.heyner.excelutils.application.ports;

import java.nio.file.Path;

public interface CorrectionImputationPort {
    void correct(Path inputFile, String sheetName);
}
