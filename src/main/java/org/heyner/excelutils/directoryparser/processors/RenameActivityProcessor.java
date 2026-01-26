package org.heyner.excelutils.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileType;
import org.heyner.excelutils.utils.filenaming.ResultNamer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Order(20)
@Slf4j
public class RenameActivityProcessor implements FileProcessor{
    private final FileClassifier classifier;
    private final ResultNamer renamer;
    @Override
    public boolean supports(Path file) {
        return classifier.classify(file) == FileType.ACTIVITY;
    }

    @Override
    public void process(Path file) throws IOException {
        log.info("Process rename activity file : {}", file);
        renamer.renameIfNeeded(file, ExcelConstants.DEFAULT_SHEET, ExcelConstants.ACTIVITY_CONTRACT_CELL);
    }
}
