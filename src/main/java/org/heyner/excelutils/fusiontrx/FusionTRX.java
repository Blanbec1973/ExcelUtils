package org.heyner.excelutils.fusiontrx;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FusionSheetMissingException;
import org.heyner.excelutils.exceptions.GracefulExitException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
@Slf4j
public class FusionTRX {
    public static void main(String[] args) {
        new FusionTRX(args);
    }

    public FusionTRX(String [] args) {

        String directoryToProcess = (args[1].equals("")) ? System.getProperty("user.dir")+"\\" : args[1];
        String pathFusion = args[2];
        log.info("Parsing directory : {}",directoryToProcess);
        log.info("Path for fusion : {}",pathFusion);

        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().contains("TRX") && file.getName().toLowerCase().endsWith(".xlsx");
        File[] listFiles = myDirectory.listFiles(filter);
        assert listFiles != null;
        if (listFiles.length == 0) {
            throw new GracefulExitException("No file to process in {}"+ directoryToProcess,0);
        }
        this.processList(listFiles, pathFusion);
        log.info("Program ends normally.");
    }

    private void processList(File[] listFiles, String pathFusion) {
        try (ExcelFile fusion = new ExcelFile(pathFusion + "FusionTRX.xlsx")) {
            Sheet sheetFusion = fusion.createSheet("Fusion");
            int rowOffset = 0;
            boolean ignoreFirstLine = false;

            for (File file : listFiles) {
                rowOffset = mergeFile(file, sheetFusion, ignoreFirstLine, rowOffset);
                ignoreFirstLine = true;
            }
            fusion.writeFichierExcel();
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

    private int mergeFile(File file, Sheet sheetFusion, boolean ignoreFirstLine, int rowOffset) {
        try (ExcelFile excelIn = new ExcelFile(file.getAbsolutePath())) {
            log.info("File {} opened.",file.getName());
            Sheet sheetIn = excelIn.getWorkBook().getSheet("sheet1");
            if (sheetIn == null) {
                throw new FusionSheetMissingException(file.getName(),-1);
            }
            return excelIn.copySheet(sheetIn, sheetFusion, ignoreFirstLine,rowOffset);
        } catch (FusionSheetMissingException e) {
          return rowOffset;
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }
}
