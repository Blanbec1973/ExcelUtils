package org.heyner.excelutils.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileRenamer;
import org.heyner.excelutils.directoryparser.FileType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RenameActivityProcessor implements FileProcessor{
    private final FileClassifier classifier;
    private final FileRenamer renamer;
    @Override
    public boolean supports(File file) {
        return classifier.classify(file) == FileType.ACTIVITY;
    }

    @Override
    public void process(File file) throws IOException {
        log.info("Process rename activity file : {}", file);
        renamer.renameActivityIfNeeded(file);
    }
}
