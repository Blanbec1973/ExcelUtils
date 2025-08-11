package org.heyner.excelutils.analyzetrx;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.FatalApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        String pathInput = args[1];
        pathModel = analyzeTRXConfig.getPathModel();
        pathResultFile=analyzeTRXConfig.getPathResultFile();
        sheetIn= analyzeTRXConfig.getSheetIn();
        sheetOut= analyzeTRXConfig.getSheetOut();
        log.info("Beginning {}",args[0]);
        cloneModel();
        transferData(pathInput);
        checkFormula(pathResultFile);
    }

    private void checkFormula(String fileName) {
        try (ExcelFile excelFile = new ExcelFile(fileName)) {
            for (Row row : excelFile.getWorkBook().getSheet(sheetOut)) {
                excelFile.evaluateFormulaCell(row.getCell(54));
                excelFile.evaluateFormulaCell(row.getCell(55));
            }
            excelFile.writeFichierExcel();
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

    private void transferData(String inputFileName) {
        try (ExcelFile excelIn = new ExcelFile(inputFileName);
        ExcelFile excelOut = new ExcelFile(pathResultFile)) {
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
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

    private void cloneModel() {
        Path in = Paths.get(pathModel);
        Path out = Paths.get(pathResultFile);
        log.info("Copy {} to {}.", pathModel, pathResultFile);
        log.debug("CurrentPath : "+System.getProperty("user.dir"));
        log.debug("Absolute path in : " + in.toAbsolutePath());
        log.debug("Absolute path out : " + out.toAbsolutePath());
        try {
            Files.copy(in, out);
        } catch (IOException e) {
            log.error("Model copy error", e);
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

}
