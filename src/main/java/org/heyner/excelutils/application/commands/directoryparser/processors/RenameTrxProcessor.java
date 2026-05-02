package org.heyner.excelutils.application.commands.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.application.commands.directoryparser.FileClassifier;
import org.heyner.excelutils.application.commands.directoryparser.FileType;
import org.heyner.excelutils.shared.utils.filenaming.ResultNamer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Order(60)
@Slf4j
public class RenameTrxProcessor implements FileProcessor{
    private final FileClassifier classifier;
    private final ResultNamer renamer;

    @Override
    public FileType getSupportedFileType() {
        return FileType.TRX;
    }

    @Override
    public void process(Path file) throws IOException {
        log.info("Process rename renameTRX file : {}", file);
        renamer.renameIfNeeded(file, ExcelConstants.DEFAULT_SHEET, ExcelConstants.TRX_CONTRACT_CELL);
    }
}
