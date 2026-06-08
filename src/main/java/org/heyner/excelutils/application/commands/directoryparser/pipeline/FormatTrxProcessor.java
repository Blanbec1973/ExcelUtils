package org.heyner.excelutils.application.commands.directoryparser.pipeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.directoryparser.FileType;
import org.heyner.excelutils.application.commands.formattrx.FormatTRX;
import org.heyner.excelutils.application.commands.formattrx.FormatTRXArgs;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Order(30)
@Slf4j
public class FormatTrxProcessor implements FileProcessor {
    private final FormatTRX formatTRX;

    @Override
    public FileType getSupportedFileType() {
        return FileType.TRX;
    }

    @Override
    public void process(Path file) {
        log.info("Process FormatTRX file : {}", file);
        formatTRX.execute(new FormatTRXArgs(file));
    }
}
