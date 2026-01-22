package org.heyner.excelutils.directoryparser.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileType;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
@Component
@RequiredArgsConstructor
@Order(50)
@Slf4j
public class FormatInvRegisterLnProcessor implements FileProcessor {
    private final FileClassifier classifier;
    private final FormatInvRegisterLN formatInvRegisterLN;

    @Override
    public boolean supports(File file) {
        return classifier.classify(file) == FileType.INV_REGISTER_LN;
    }

    @Override
    public void process(File file) throws IOException {
        log.info("Process InvRegisterLN file : {}", file);
        String [] argFile = { file.toString()};
        formatInvRegisterLN.execute(argFile);
    }
}
