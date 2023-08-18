package CorrectionImputation;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import renamePSA.renamePSA;

import java.io.IOException;
import java.util.Iterator;


public class CorrectionImputation {
    private static final Logger logger = LogManager.getLogger(renamePSA.class);
    private static FichierExcel fichierExcel;

    public static void main(String[] args) throws IOException {
        logger.info("Démarrage CorrectionImputation");
        // args[0] : parsing file
        // args[1] : sheet

        fichierExcel = new FichierExcel(args[0]);
        logger.info("Fichier à traiter : " + args[0]);

        int numligne = 0;
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(args[1]);
        fichierExcel.getWorkBook().setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        //Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = dataSheet.iterator();
        for (Row row : dataSheet) {
           Cell cell = row.getCell(56);
           if (cell != null && cell.getCellType() == CellType.STRING) {
                System.out.println("numligne " + numligne + " Valeur : "+ cell.getStringCellValue());
                traiteLigne(row);
            }

            //for (Cell cell : row) {
            //    //System.out.print(cell + "| ");
            //}
            numligne = numligne+1;
        }
        fichierExcel.writeFichierExcel();
    }

    private static void traiteLigne(Row row) {
        //8 : code   476867
        //9 : nom    POMMERET
        Cell cell = row.getCell(56);

        System.out.println("traiteLigne *"+cell.getStringCellValue()+"*");
        System.out.println("traiteLigne *"+row.getCell(8).getStringCellValue()+"*");

        if (cell.getStringCellValue().equals("-Difficulté à déterminer-") &&
                row.getCell(8).getStringCellValue().equals("476867")) {
            int numligne = row.getRowNum()+1;
            String formula = "AC" + String.valueOf(numligne)+"/8" ;
            System.out.println(formula);
            cell.setCellFormula(formula);
            fichierExcel.evaluateFormulaCell(cell);

        }





    }


}
