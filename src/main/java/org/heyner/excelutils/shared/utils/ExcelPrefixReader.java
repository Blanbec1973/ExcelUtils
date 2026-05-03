package org.heyner.excelutils.shared.utils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.TransferDataException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ExcelPrefixReader implements PrefixReader {

    public String read(String fileName, String sheet, String cell) {
        try (ExcelFile fichierExcel = ExcelFile.open(fileName)) {
            String prefix = fichierExcel.getCellValue(sheet,cell);
            log.debug("prefix : {}", prefix);
            return prefix;
        } catch (IOException e) {
            log.error("Unable to read file {}", fileName, e);
            throw new TransferDataException(
                    "Unable to read prefix from file: " + fileName,
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR
            );
        }
    }
}
