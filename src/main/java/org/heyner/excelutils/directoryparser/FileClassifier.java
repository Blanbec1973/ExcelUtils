
package org.heyner.excelutils.directoryparser;

import org.heyner.excelutils.ExcelConstants;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FileClassifier {

    public FileType classify(Path f) {
        String path = f.toString();
        if (path.contains(ExcelConstants.ACTIVITY_SHEET))      return FileType.ACTIVITY;
        if (path.contains(ExcelConstants.TRX_SHEET))           return FileType.TRX;
        if (path.contains(ExcelConstants.INV_REGISTER_LN_SHEET)) return FileType.INV_REGISTER_LN;
        return FileType.UNKNOWN;
    }
}
