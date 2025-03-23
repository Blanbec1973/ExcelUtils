package format_trx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.ExcelFile;

import java.io.IOException;

public class FormatTRX {
    private static final Logger logger = LogManager.getLogger(FormatTRX.class);

    private FormatTRX() {

    }

    public static void applyFormatTRX(String[] args) {
        logger.info("File to process : {}", args[0]);
        try (ExcelFile fichierExcel = new ExcelFile(args[0])) {
            fichierExcel.deleteFirstLineContaining("sheet1","Transaction analysis");
            fichierExcel.writeFichierExcel();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }


}
