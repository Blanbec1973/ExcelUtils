package commun;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import renamePSA.renamePSA;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FichierExcel extends FileInputStream {
    private static final Logger logger = LogManager.getLogger(renamePSA.class);
    private final Workbook workbook;
    private final String name;
    private final  XSSFFormulaEvaluator formulaEvaluator;

    public FichierExcel(String name) throws IOException {
        super(name);
        this.name = name;
        workbook = new XSSFWorkbook(this);
        formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
    }

    public void evaluateFormulaCell(Cell cell) {
        formulaEvaluator.evaluateFormulaCell(cell);
    }
    public void writeFichierExcel() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(this.name);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public String getCellValue(String sheet, String address) {
        Sheet dataSheet = workbook.getSheet(sheet);
        //logger.info(dataSheet.toString());
        CellReference cellReference = new CellReference(address);
        Cell cell = dataSheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
        if (cell.getCellType() == CellType.STRING) {
            logger.info(cell.getStringCellValue());
        }
        return cell.getStringCellValue();
    }

    public String getCellValue(String sheet, int numCol, int numLigne) {
        Sheet dataSheet = workbook.getSheet(sheet);
        Cell cell = dataSheet.getRow(numLigne).getCell(numCol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.STRING) {
            logger.info(cell.getStringCellValue());
        }
        return cell.toString();
    }

    public Workbook getWorkBook() {
        return workbook;
    }
}
