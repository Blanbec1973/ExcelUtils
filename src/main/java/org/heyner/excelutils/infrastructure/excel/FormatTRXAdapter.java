package org.heyner.excelutils.infrastructure.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.ports.FormatTRXPort;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormatTRXAdapter implements FormatTRXPort {
    private static final String FORMATTING_TRX_FILE_LOG = "Formatting TRX file : {}";
    private static final String ERROR_PROCESSING_FILE_LOG = "Error processing file: {}";

    @Override
    public void deleteFirstLineContaining(Path inputFile, String sheet, String header) {
        log.info(FORMATTING_TRX_FILE_LOG, inputFile);
        try (ExcelFile fichierExcel = ExcelFile.open(inputFile.toString())) {
            log.debug("Opening workbook {}", inputFile);
            fichierExcel.deleteFirstLineContaining(sheet, header);
            log.debug("Deleting header '{}' from sheet {}", header, sheet);
            fichierExcel.writeFichierExcel();
            log.info("TRX file formatted successfully: {}", inputFile);
        } catch (IOException e) {
            log.error(ERROR_PROCESSING_FILE_LOG, inputFile, e);
            throw new FatalApplicationException(
                    "Unable to format TRX file.",
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR);

        }
    }
}
