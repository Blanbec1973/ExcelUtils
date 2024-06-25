package format_trx;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FormatTRX {
    private static final Logger logger = LogManager.getLogger(format_trx.FormatTRX.class);

    public static void main(String[] args) throws IOException {
        try (FichierExcel fichierExcel = new FichierExcel(args[0])) {
            logger.info("File to process : {}", args[0]);
            fichierExcel.deleteFirstLineContaining("sheet1","Transaction analysis");
            fichierExcel.writeFichierExcel();
        } catch (FileNotFoundException e) {
            logger.error("Error with file : {}",e.getMessage());
            System.exit(-1);
        }
    }


}
