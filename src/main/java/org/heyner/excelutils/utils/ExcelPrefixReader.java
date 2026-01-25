package org.heyner.excelutils.utils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.springframework.stereotype.Component;

import java.io.File;
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
            log.error("Unable to read file {} : {}", fileName,e.getMessage() );
        }
        return null;
    }
}
