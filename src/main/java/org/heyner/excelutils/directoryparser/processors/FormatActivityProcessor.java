package org.heyner.excelutils.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileType;
import org.heyner.excelutils.formatactivity.FormatActivity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Component
@Order(10)
@RequiredArgsConstructor
public class FormatActivityProcessor implements FileProcessor {

    private final FileClassifier classifier;
    private final FormatActivity formatActivity;

    @Override
    public boolean supports(Path file) {
        return classifier.classify(file) == FileType.ACTIVITY;
    }

    @Override
    public void process(Path file) throws IOException {
        log.info("ActivityFormatProcessor: {}", file.toString());
        formatActivity.execute(file.toString());
    }
}

