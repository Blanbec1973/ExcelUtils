package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.ExcelFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FileNameGenerator {

    private FileNameGenerator() {}
    public static String generateFileNameWithDate(String template) {
        LocalDate today = LocalDate.now();
        String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return template.replace(ExcelConstants.DATE_TEMPLATE, date);
    }
    public static void renamePSA (File file, String sheet, String cell) {
        try (ExcelFile fichierExcel = new ExcelFile(file.toString())) {
            String prefix = fichierExcel.getCellValue(sheet,cell);
            log.debug("prefix : {}", prefix);

            String parentPath = file.getAbsoluteFile().getParent();
            String newName = parentPath + "/" + prefix + "-" + file.getName();
            log.debug("newName : {}",newName);

            File dest = new File(newName);
            if (file.renameTo(dest)) log.debug("New name : {}", newName);
        } catch (IOException e) {
            log.error("Unable to rename {} : {}", file,e.getMessage() );
        }
    }
    public static boolean hasFileNoPrefix(File fichier) {
        String fileName = fichier.getName();
        if (fileName.length()<15) return true;
        String prefix = fileName.substring(0,15);
        return (!prefix.matches("\\d+")) ;
    }

}
