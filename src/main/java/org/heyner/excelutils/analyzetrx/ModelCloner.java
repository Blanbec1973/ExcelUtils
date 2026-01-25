package org.heyner.excelutils.analyzetrx;

import java.nio.file.Path;

public interface ModelCloner {
    void copy(Path in, Path out);
}
