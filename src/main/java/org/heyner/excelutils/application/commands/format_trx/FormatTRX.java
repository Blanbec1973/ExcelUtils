package org.heyner.excelutils.application.commands.format_trx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@Slf4j
public class FormatTRX implements Command<FormatTRXArgs> {

    private static final String FORMATTING_TRX_FILE_LOG = "Formatting TRX file : {}";
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
    public void execute(FormatTRXArgs args) {
        log.info(FORMATTING_TRX_FILE_LOG, args.inputFile());
        try (ExcelFile fichierExcel = ExcelFile.open(args.inputFile().toString())) {
            log.debug("Opening workbook {}", args.inputFile());
            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET,"Transaction analysis");
            log.debug("Deleting header '{}' from sheet {}", "Transaction analysis", ExcelConstants.DEFAULT_SHEET);
            fichierExcel.writeFichierExcel();
            log.info("TRX file formatted successfully: {}", args.inputFile());
        } catch (IOException e) {
            log.error(ERROR_PROCESSING_FILE_LOG, args.inputFile(), e);
            throw new FatalApplicationException(
                    "Unable to format TRX file.",
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR
            );
        }
    }
}
