package org.heyner.excelutils.analyzetrx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class AnalyzeTRX implements CommandService {
    private final AnalyzeTRXConfig analyzeTRXConfig;
    private String pathModel;
    private String pathResultFile;
    private String sheetIn;
    private String sheetOut;

    @Autowired
    public AnalyzeTRX(AnalyzeTRXConfig analyzeTRXConfig) {
        this.analyzeTRXConfig = analyzeTRXConfig;
    }

    @Override
    public String getCommandName() {
        return "analyzetrx";
    }

    @Override
    public void execute(String... args) {
        long start = System.currentTimeMillis();
        String currentPath = System.getProperty("user.dir");
        pathModel = currentPath+analyzeTRXConfig.getPathModel();
        pathResultFile=currentPath+analyzeTRXConfig.getPathResultFile();
        sheetIn= analyzeTRXConfig.getSheetIn();
        sheetOut= analyzeTRXConfig.getSheetOut();
        log.info("Beginning {}",args[0]);
        cloneModel();
        transferData(args[1]);
        checkFormula(pathResultFile);

        log.info("Program ends normally in {} ms.", System.currentTimeMillis()-start);

    }

    private void checkFormula(String fileName) {
        try (ExcelFile excelFile = new ExcelFile(fileName)) {
            for (Row row : excelFile.getWorkBook().getSheet(sheetOut)) {
                excelFile.evaluateFormulaCell(row.getCell(54));
                excelFile.evaluateFormulaCell(row.getCell(55));
            }
            excelFile.writeFichierExcel();
        } catch (IOException e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
        }
    }

    private void cloneModel() {
        File in = new File(pathModel);
        File out = new File(pathResultFile);
        log.info("Copy {} to {}.",in.getAbsolutePath(),out.getAbsolutePath());
        try {
            FileUtils.copyFile(in, out);
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }



}
