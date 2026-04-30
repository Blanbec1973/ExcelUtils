package org.heyner.excelutils.format_trx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
@Slf4j
public class FormatTRX implements CommandService {

    private static final String FILE_TO_PROCESS_LOG = "File to process: {}";
    private static final String ERROR_PROCESSING_FILE_LOG = "Error processing file: {}";

    @Override
    public String getCommandName() {
        return "formattrx";
    }
    @Override
    public void execute(String... args) throws IOException {
        FormatTRXArgs parsed = mapArgs(args);
        execute(parsed);
    }

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

    private FormatTRXArgs mapArgs(String[] args) {
        return new FormatTRXArgs(
                java.nio.file.Path.of(args[1])
        );
    }
}
