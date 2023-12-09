package directoryParser;

import FormatActivity.FormatActivity;
import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Objects;

public class DirectoryParser {
    private static final Logger logger = LogManager.getLogger(DirectoryParser.class);
    private final File[] listFiles;

    public static void main(String[] args) throws IOException {

        String directoryToProcess = (args.length == 0) ? System.getProperty("user.dir")+"\\" : args[0];
        DirectoryParser directoryParser = new DirectoryParser(directoryToProcess);

        if (directoryParser.isListFilesEmpty()) {
            logger.info("No file to process in {}", directoryToProcess);
            System.exit(0);
        }
        directoryParser.processList();
    }

    public DirectoryParser(String directory) {
        File myDirectory = new File(directory);
        FileFilter filter = (file) -> file.getName().toLowerCase().endsWith(".xlsx");
        listFiles = myDirectory.listFiles(filter);
    }

    public boolean isListFilesEmpty() { return (listFiles.length == 0);}

    public void processList() throws IOException {
        for (File file : Objects.requireNonNull(listFiles)) {
            logger.info("ProcessList file : {}", file);
            if (file.toString().matches(".*AR_ITEM_ACTIVITY.*"))
                processActivity(file);
            if (file.toString().matches(".*AR_ITEM_ACTIVITY.*"))
                processActivityRename(file);
            if (file.toString().matches(".*UC_PCB_PROJ_TRX.*"))
                processTrxRename(file);
        }

    }

    public void processActivity(File file) throws IOException {
        logger.info("Process activity file : {}", file);
        String [] activityFile = { file.toString()};
        FormatActivity.main(activityFile);
    }
    public void processActivityRename(File file) throws IOException {
        logger.info("Process renameACtivity file : {}", file);
        renamePSA(file,"sheet1", "G3");
    }

    public void processTrxRename(File file) throws IOException {
        logger.info("Process renameTRX file : {}", file);
        renamePSA(file, "sheet1", "B3");
    }

    public void renamePSA (File file, String sheet, String cell) throws IOException {
        FichierExcel fichierExcel = new FichierExcel(file.toString());
        String prefix = fichierExcel.getCellValue(sheet,cell);
        fichierExcel.close();
        String newName = file.getParent() + "\\"+ prefix + "-"+file.getName();
        logger.info("Nouveau nom : {}", newName);
        File dest = new File(newName);
        if (file.renameTo(dest)) logger.info("Nouveau nom : {}", newName);
    }
}
