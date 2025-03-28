package analyzetrx;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.ExcelFile;
import org.heyner.common.Parameter;


import java.io.File;
import java.io.IOException;

public class AnalyzeTRX {
    private static final Logger logger = LogManager.getLogger(AnalyzeTRX.class);
    private final Parameter param = new Parameter("config.properties");
    private final String currentPath = System.getProperty("user.dir");
    private final String pathModel = currentPath+param.getProperty("AnalyzeTRX.pathModel");
    private final String pathResultFile=currentPath+param.getProperty("AnalyzeTRX.pathResultFile");
    private final String sheetIn= param.getProperty("AnalyzeTRX.sheetIn");
    private final String sheetOut= param.getProperty("AnalyzeTRX.sheetOut");

    public static void main(String[] args) {
        if (args.length == 0) {
            logger.error("No argument, end of program.");
            System.exit(-1);
        }
        new AnalyzeTRX(args);
    }

    public AnalyzeTRX(String [] args) {
        long start = System.currentTimeMillis();
        logger.info("Beginning {}",args[0]);
        cloneModel();

        transferData(args[1]);

        checkFormula(pathResultFile);

        logger.info("Program ends normally in {} ms.", System.currentTimeMillis()-start);
    }

    private void checkFormula(String fileName) {
        try (ExcelFile excelFile = new ExcelFile(fileName)) {
            for (Row row : excelFile.getWorkBook().getSheet(sheetOut)) {
                excelFile.evaluateFormulaCell(row.getCell(54));
                excelFile.evaluateFormulaCell(row.getCell(55));
            }
            excelFile.writeFichierExcel();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


    }

    private void transferData(String fileName) {
        try (ExcelFile excelIn = new ExcelFile(fileName);
        ExcelFile excelOut = new ExcelFile(System.getProperty("user.dir") + "/Analyze TRX.xlsx")) {
            Integer rowCount = excelIn.rowCount(sheetIn, 0);

            // Creating Range A2:BB3000)
            excelIn.setTileRange(new CellRangeAddress(1, rowCount - 1, 0, 53));
            excelOut.setTileRange(new CellRangeAddress(1, rowCount - 1, 0, 53));
            excelIn.copyRange(excelOut, sheetIn, sheetOut);

            // Creating Range A2:BB3000)
            excelIn.setTileRange(new CellRangeAddress(1, rowCount - 1, 56, 56));
            excelOut.setTileRange(new CellRangeAddress(1, rowCount - 1, 56, 56));
            excelIn.copyRange(excelOut, sheetIn, sheetOut);

            excelOut.writeFichierExcel();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void cloneModel() {
        File in = new File(pathModel);
        File out = new File(pathResultFile);
        logger.info("Copy {} to {}.",in.getAbsolutePath(),out.getAbsolutePath());
        try {
            FileUtils.copyFile(in, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
    }


}
