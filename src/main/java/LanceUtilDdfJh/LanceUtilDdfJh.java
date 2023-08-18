package LanceUtilDdfJh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LanceUtilDdfJh {

    private static final Logger logger = LogManager.getLogger(LanceUtilDdfJh.class);

    public static void main(String[] args) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(args[0]));
        Workbook workbook = new XSSFWorkbook(excelFile);
        //Récupération de la première feuille de calcul du document
        Sheet dataSheet = workbook.getSheet("Paramétrage");
        logger.info(dataSheet.getSheetName());
        //Cell cell = dataSheet.getRow(2).getCell(1);
        //if (cell.getCellType() == CellType.STRING) {
        //    logger.info(cell.getStringCellValue());
        //}
        excelFile.close();
    }
}