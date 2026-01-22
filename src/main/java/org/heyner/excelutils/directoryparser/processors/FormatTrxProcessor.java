package org.heyner.excelutils.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileType;
import org.heyner.excelutils.format_trx.FormatTRX;
import org.heyner.excelutils.formatactivity.FormatActivity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(30)
@Slf4j
public class FormatTrxProcessor implements FileProcessor {
    private final FileClassifier classifier;
    private final FormatTRX formatTRX;

    @Override
    public boolean supports(File file) {
        return classifier.classify(file) == FileType.TRX;
    }

    @Override
    public void process(File file) throws IOException {
        log.info("Process FormatTRX file : {}", file);
        String [] trxFile = { file.toString()};
        formatTRX.execute(trxFile);
    }
}
