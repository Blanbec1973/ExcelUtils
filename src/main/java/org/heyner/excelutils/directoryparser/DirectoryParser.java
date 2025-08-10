package org.heyner.excelutils.directoryparser;


import lombok.extern.slf4j.Slf4j;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.GracefulExitException;
import org.heyner.excelutils.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.format_trx.FormatTRX;
import org.heyner.excelutils.formatactivity.FormatActivity;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class DirectoryParser implements CommandService {
    public static final String SHEET_1 = "sheet1";
    public static final String UC_PCB_PROJ_TRX = "UC_PCB_PROJ_TRX";
    public static final String AR_ITEM_ACTIVITY = "AR_ITEM_ACTIVITY";
    private final CorrectionImputation correctionImputation;
    private final FormatActivity formatActivity;

    private final FormatInvRegisterLN formatInvRegisterLN;
    private File[] listFiles;

    @Autowired
    public DirectoryParser (CorrectionImputation correctionImputation, FormatActivity formatActivity, FormatInvRegisterLN formatInvRegisterLN) {
        this.correctionImputation = correctionImputation;
        this.formatActivity = formatActivity;
        this.formatInvRegisterLN = formatInvRegisterLN;
    }
    @Override
    public String getCommandName() {
        return "directoryparser";
    }

    @Override
    public void execute(String... args) throws IOException {
        String directoryToProcess = args[1];
        log.debug("Beginning function : {}",
                this.getClass().getSimpleName());
        log.info("Processing {}",directoryToProcess);
        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".xlsx");
        listFiles = myDirectory.listFiles(filter);

        if (isListFilesEmpty()) {
            throw new GracefulExitException("No file to process in " + directoryToProcess, 0);
        }
        processList();
    }

    public boolean isListFilesEmpty() { return (listFiles.length == 0);}

    public void processList() throws IOException {
        for (File file : Objects.requireNonNull(listFiles)) {
            log.info("ProcessList file : {}", file.getName());
            boolean isActivity = file.toString().contains(AR_ITEM_ACTIVITY);
            boolean isTrx = file.toString().contains(UC_PCB_PROJ_TRX);
            boolean isInvRegisterLN = file.toString().contains("UC_PCB_MS_INV_REGISTER_LN");

            if (isActivity)
                processActivity(file);
            if (isActivity)
                processActivityRename(file);
            if (isTrx)
                processCorrectionImputation(file);
            if (isTrx)
                processFormatTRX(file);
            if (isTrx)
                processTrxRename(file);
            if (isInvRegisterLN)
                processInvRegisterLN(file);
        }
    }

    private void processInvRegisterLN(File file) throws IOException {
        log.info("Process InvRegisterLN file : {}", file);
        String [] argFile = { file.toString()};
        formatInvRegisterLN.execute(argFile);
    }

    public void processFormatTRX(File file) {
        log.info("Process FormatTRX file : {}", file);
        String [] trxFile = { file.toString()};
        FormatTRX formatTRX = new FormatTRX();
        formatTRX.execute(trxFile);
    }
    public void processActivity(File file) throws IOException {
        log.info("Process activity file : {}", file);
        String [] activityFile = { file.toString()};
        formatActivity.execute(activityFile);
    }
    public void processActivityRename(File file) throws IOException {
        log.info("Process rename activity file : {}", file);
        if (checkAbsenceOfPrefix(file))
            renamePSA(file,SHEET_1,"G3");
    }
    public void processTrxRename(File file) throws IOException {
        log.info("Process renameTRX file : {}", file);
        if (checkAbsenceOfPrefix(file))
            renamePSA(file,SHEET_1,"B3");
    }
    public void processCorrectionImputation(File file) throws IOException {
        log.info("Process correction imputationTRX file : {}", file);
        String [] trxFile = { file.toString(),SHEET_1};
        correctionImputation.execute(trxFile);
    }
    public void renamePSA (File file, String sheet, String cell) throws IOException {
        ExcelFile fichierExcel = new ExcelFile(file.toString());
        String prefix = fichierExcel.getCellValue(sheet,cell);
        fichierExcel.close();
        String newName = file.getParent() + "/"+ prefix + "-"+file.getName();
        File dest = new File(newName);
        if (file.renameTo(dest)) log.info("Nouveau nom : {}", newName);
    }
    private boolean checkAbsenceOfPrefix(File fichier) {
        String fileName = fichier.getName();
        if (fileName.length()<15) return true;
        String prefix = fileName.substring(0,15);
        return (!prefix.matches("\\d+")) ;
    }
}
