package org.heyner.excelutils.application.commands.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.application.commands.directoryparser.FileType;
import org.heyner.excelutils.shared.utils.filenaming.ResultNamer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Order(20)
@Slf4j
public class RenameActivityProcessor implements FileProcessor{
    private final ResultNamer renamer;

    @Override
    public FileType getSupportedFileType() {
        return FileType.ACTIVITY;
    }

    @Override
    public void process(Path file) {
        log.info("Process rename activity file : {}", file);
        renamer.renameIfNeeded(file, ExcelConstants.DEFAULT_SHEET, ExcelConstants.ACTIVITY_CONTRACT_CELL);
    }
}
