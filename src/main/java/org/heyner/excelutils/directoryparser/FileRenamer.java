package org.heyner.excelutils.directoryparser;

        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.heyner.excelutils.ExcelConstants;
        import org.heyner.excelutils.FileNameGenerator;
        import org.springframework.stereotype.Component;

        import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileRenamer {

    public void renameActivityIfNeeded(File file) {
        if (FileNameGenerator.hasFileNoPrefix(file)) {
            FileNameGenerator.renamePSA(file, ExcelConstants.DEFAULT_SHEET, ExcelConstants.ACTIVITY_CONTRACT_CELL);
        }
    }

    public void renameTrxIfNeeded(File file) {
        if (FileNameGenerator.hasFileNoPrefix(file)) {
            FileNameGenerator.renamePSA(file, ExcelConstants.DEFAULT_SHEET, ExcelConstants.TRX_CONTRACT_CELL);
        }
    }
}

