package directoryparser;

import commun.ProgramId;
import correctionimputation.CorrectionImputation;
import format_trx.FormatTRX;
import formatactivity.FormatActivity;
import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import renamepsa.RenamePSA;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Objects;

public class DirectoryParser {
    private static final Logger logger = LogManager.getLogger(DirectoryParser.class);
    public static final String SHEET_1 = "sheet1";
    public static final String UC_PCB_PROJ_TRX = ".*UC_PCB_PROJ_TRX.*";
    public static final String AR_ITEM_ACTIVITY = ".*AR_ITEM_ACTIVITY.*";
    private final File[] listFiles;

    public static void main(String[] args) throws IOException {
        logger.info("Beginning :{} version:{}", ProgramId.NAME, ProgramId.VERSION);

        String directory = (args.length == 0) ? System.getProperty("user.dir")+"\\" : args[0];
        new DirectoryParser(directory);
    }

    public DirectoryParser(String directoryToProcess) throws IOException {
        logger.info("Processing {}",directoryToProcess);
        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".xlsx");
        listFiles = myDirectory.listFiles(filter);

        if (isListFilesEmpty()) {
            logger.info("No file to process in {}", directoryToProcess);
            System.exit(0);
        }
        processList();
    }

    public boolean isListFilesEmpty() { return (listFiles.length == 0);}

    public void processList() throws IOException {
        for (File file : Objects.requireNonNull(listFiles)) {
            logger.info("ProcessList file : {}", file.getName());
            if (file.toString().matches(AR_ITEM_ACTIVITY))
                processActivity(file);
            if (file.toString().matches(AR_ITEM_ACTIVITY))
                processActivityRename(file);
            if (file.toString().matches(UC_PCB_PROJ_TRX))
                processCorrectionImputation(file);
            if (file.toString().matches(UC_PCB_PROJ_TRX))
                processFormatTRX(file);
            if (file.toString().matches(UC_PCB_PROJ_TRX))
                processTrxRename(file);
        }
    }

    public void processFormatTRX(File file) throws IOException {
        logger.info("Process FormatTRX file : {}", file);
        String [] trxFile = { file.toString()};
        FormatTRX.main(trxFile);
    }
    public void processActivity(File file) throws IOException {
        logger.info("Process activity file : {}", file);
        String [] activityFile = { file.toString()};
        FormatActivity.main(activityFile);
    }
    public void processActivityRename(File file) throws IOException {
        logger.info("Process rename activity file : {}", file);
        if (RenamePSA.checkAbsenceOfPrefix(file))
            renamePSA(file,SHEET_1,"G3");
    }

    public void processTrxRename(File file) throws IOException {
        logger.info("Process renameTRX file : {}", file);
        if (RenamePSA.checkAbsenceOfPrefix(file))
            renamePSA(file,SHEET_1,"B3");
    }
    public void processCorrectionImputation(File file) throws IOException {
        logger.info("Process correction imputationTRX file : {}", file);
        String [] trxFile = { file.toString(),SHEET_1};
        CorrectionImputation.main(trxFile);
    }
    public void renamePSA (File file, String sheet, String cell) throws IOException {
        FichierExcel fichierExcel = new FichierExcel(file.toString());
        String prefix = fichierExcel.getCellValue(sheet,cell);
        fichierExcel.close();
        String newName = file.getParent() + "\\"+ prefix + "-"+file.getName();
        logger.info("Nouveau nom1 : {}", newName);
        File dest = new File(newName);
        if (file.renameTo(dest)) logger.info("Nouveau nom2 : {}", newName);
    }
}
