package obmsinvoicing;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.ExcelFile;
import org.heyner.common.Parameter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObmsInvoicing {
    private static final Logger logger = LogManager.getLogger(ObmsInvoicing.class);
    private final String currentPath = System.getProperty("user.dir");
    private final Parameter param = new Parameter("config.properties");
    private final String pathModel = currentPath+param.getProperty("ObmsInvoicing.pathModel");
    private final String pathResultFile=currentPath+param.getProperty("ObmsInvoicing.pathResultFile");
    private final File[] listFiles;

    public static void main(String[] args) {
        String directory = (args.length == 0) ? System.getProperty("user.dir")+"\\" : args[0];
        new ObmsInvoicing(directory);

    }
    public ObmsInvoicing(String directoryToProcess) {

        String projectName = param.getProperty("projectName");
        String version = param.getProperty("version");
        logger.info("Beginning : {} version {} function {}",
                projectName,
                version,
                this.getClass().getSimpleName());

        logger.info("Processing {}",directoryToProcess);
        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".pdf");
        listFiles = myDirectory.listFiles(filter);

        if (listFiles.length == 0) {
            logger.info("No file to process in {}", directoryToProcess);
            System.exit(0);
        }
        List<ObmsInvoice> invoiceList = new ArrayList<>();
        processList(invoiceList);
        cloneModel();
        transferData(invoiceList);
    }

    private void transferData(List<ObmsInvoice> invoiceList) {
        try (ExcelFile excelOut = new ExcelFile("Invoicing.xlsm")) {
            Sheet sheet = excelOut.getWorkBook().getSheet("Facturation uniquement");
            transferRows(invoiceList, sheet);
            excelOut.writeFichierExcel();
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
    }

    private void transferRows(List<ObmsInvoice> invoiceList, Sheet sheet) {
        int rowNum = 4;
        int invoiceNum = 1;

        for (ObmsInvoice invoice : invoiceList) {
            if (logger.isInfoEnabled())
                logger.info("Invoice description ==> {}", invoice.buildInvoiceDescription());
            Row row = sheet.getRow(rowNum++);
            transferCellValue(row,0,"EU005");
            transferCellValue(row,1,"300000000111021");
            transferCellValue(row,2,"BDC00");
            transferCellValue(row,3, String.valueOf(invoiceNum++));
            transferCellValue(row, 4,invoice.buildInvoiceDescription());
            //TODO transferCellValue(row,5,invoice.getNumberOfDays());
            transferCellValue(row, 6,"DAY = Day");
            //TODO transferCellValue(row, 7,invoice.getPricePerDay());
            //TODO row.createCell(8).setCellFormula("F"+rowNum+"*H"+rowNum);
            //TODO DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            //TODO transferCellValue(row, 10,invoice.getDateFrom().format(formatter));
            //TODO transferCellValue(row, 11,invoice.getDateTo().format(formatter));
        }
    }

    private void transferCellValue(Row row, int i, String cellValue) {
        Cell cell = row.getCell(i);
        CellStyle cellStyle = cell.getCellStyle();
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
    }

    private void processList(List<ObmsInvoice> invoiceList) {
        for (File file : Objects.requireNonNull(listFiles)) {
            logger.info("ProcessList file : {}", file.getName());
            invoiceList.add(new ObmsInvoice(file));
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
