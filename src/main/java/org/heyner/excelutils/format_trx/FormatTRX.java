package org.heyner.excelutils.format_trx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
@Slf4j
public class FormatTRX implements CommandService {
    @Override
    public String getCommandName() {
        return "formattrx";
    }
    @Override
    public void execute(String... args) {
        log.info("File to process : " + args[0]);
        try (ExcelFile fichierExcel = new ExcelFile(args[0])) {
            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET,"Transaction analysis");
            fichierExcel.writeFichierExcel();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
