package org.heyner.excelutils.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Order(40)
@Slf4j
public class CorrectionImputationProcessor implements FileProcessor {
    private final FileClassifier classifier;
    private final CorrectionImputation correctionImputation;

    @Override
    public boolean supports(Path file) {
        return classifier.classify(file) == FileType.TRX;
    }

    @Override
    public void process(Path file) throws IOException {
        log.info("Process correction imputationTRX file : {}", file);
        String [] trxFile = { file.toString(), ExcelConstants.DEFAULT_SHEET};
        correctionImputation.execute(trxFile);
    }
}
