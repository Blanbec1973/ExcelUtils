package fusiontrx;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class FusionTRX {
    private static final Logger logger = LogManager.getLogger(FusionTRX.class);
    public static void main(String[] args) throws IOException {
        new FusionTRX(args);
    }

    public FusionTRX(String [] args) throws IOException {

        String directoryToProcess = (args[1].equals("")) ? System.getProperty("user.dir")+"\\" : args[1];
        String pathFusion = args[2];
        logger.info("Parsing directory : {}",directoryToProcess);
        logger.info("Path for fusion : {}",pathFusion);

        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().contains("TRX") && file.getName().toLowerCase().endsWith(".xlsx");
        File[] listFiles = myDirectory.listFiles(filter);
        assert listFiles != null;
        if (listFiles.length == 0) {
            logger.info("No file to process in {}", directoryToProcess);
            System.exit(0);
        }
        this.processList(listFiles, pathFusion);
        logger.info("Program ends normally.");
    }

    private void processList(File[] listFiles, String pathFusion) throws IOException {
        FichierExcel fusion = new FichierExcel(pathFusion + "FusionTRX.xlsx");
        Sheet sheetFusion = fusion.createSheet("Fusion");
        Integer rowOffset = 0;
        boolean ignoreFirstLine = false;

        for (File file : listFiles) {
            FichierExcel excelIn = new FichierExcel(file.getAbsolutePath());
            logger.info("File {} opened.",file.getName());
            Sheet sheetIn = excelIn.getWorkBook().getSheet("sheet1");
            rowOffset=excelIn.copySheet(sheetIn, sheetFusion, ignoreFirstLine,rowOffset);
            ignoreFirstLine = true;
        }
        fusion.writeFichierExcel();
    }


}
