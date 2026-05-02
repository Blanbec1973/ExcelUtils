package org.heyner.excelutils.application.commands.format_trx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@Slf4j
public class FormatTRX implements Command<FormatTRXArgs> {

    private static final String FILE_TO_PROCESS_LOG = "File to process: {}";
    private static final String ERROR_PROCESSING_FILE_LOG = "Error processing file: {}";

    @Override
    public String name() {
        return "formattrx";
    }

    @Override
    public FormatTRXArgs parse(String[] args) {
        return new FormatTRXArgs(Path.of(args[1]));
    }

    @Override
    public void execute(FormatTRXArgs args) throws IOException {
        log.info(FILE_TO_PROCESS_LOG, args.inputFile());
        try (ExcelFile fichierExcel = ExcelFile.open(args.inputFile().toString())) {
            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET,"Transaction analysis");
            fichierExcel.writeFichierExcel();
        } catch (IOException e) {
            log.error(ERROR_PROCESSING_FILE_LOG, args.inputFile(), e);
            throw e;
        }
    }
}
