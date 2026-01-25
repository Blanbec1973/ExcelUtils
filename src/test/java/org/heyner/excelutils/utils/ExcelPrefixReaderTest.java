package org.heyner.excelutils.utils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class ExcelPrefixReaderTest {

    public String read(File f, String sheet, String cell) {
        try (ExcelFile fichierExcel = ExcelFile.open(f.toString())) {
            String prefix = fichierExcel.getCellValue(sheet,cell);
            log.debug("prefix : {}", prefix);
            return prefix;
        } catch (IOException e) {
            log.error("Unable to read file {} : {}", f,e.getMessage() );
        }
        return null;
    }
}
