package org.heyner.excelutils.application.commands.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationArgs;
import org.heyner.excelutils.application.commands.directoryparser.FileClassifier;
import org.heyner.excelutils.application.commands.directoryparser.FileType;
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
    public FileType getSupportedFileType() {
        return FileType.TRX;
    }

    @Override
    public void process(Path file) throws IOException {
        log.info("Process correction imputationTRX file : {}", file);

        CorrectionImputationArgs args = CorrectionImputationArgs.builder()
                .inputFile(Path.of(file.toString()))
                .sheetName(ExcelConstants.DEFAULT_SHEET)
                .build();
        correctionImputation.execute(args);
    }
}
