package org.heyner.excelutils.directoryparser;


import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.FileNameGenerator;
import org.heyner.excelutils.exceptions.FileHandlingException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.format_trx.FormatTRX;
import org.heyner.excelutils.formatactivity.FormatActivity;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class DirectoryParser implements CommandService {
    private final CorrectionImputation correctionImputation;
    private final FormatActivity formatActivity;
    private final FormatInvRegisterLN formatInvRegisterLN;

    private final FormatTRX formatTRX;
    private File[] listFiles;


    private final List<FileHandler> handlers = List.of(
            // Activity : formater
            new FileHandler() {
                @Override public boolean supports(File f) {
                    return isActivity(f);
                }
                @Override public void run(File f) throws IOException {
                    processActivity(f);
                }
            },
            // Activity : rename si pas de prefix
            new FileHandler() {
                @Override public boolean supports(File f) {
                    return isActivity(f);
                }
                @Override public void run(File f) {
                    processActivityRename(f);
                }
            },
            // TRX : correction imputation
            new FileHandler() {
                @Override public boolean supports(File f) {
                    return isTrx(f);
                }
                @Override public void run(File f) throws IOException {
                    processCorrectionImputation(f);
                }
            },
            // TRX : format TRX
            new FileHandler() {
                @Override public boolean supports(File f) {
                    return isTrx(f);
                }
                @Override public void run(File f) {
                    processFormatTRX(f);
                }
            },
            // TRX : rename
            new FileHandler() {
                @Override public boolean supports(File f) {
                    return isTrx(f);
                }
                @Override public void run(File f) {
                    processTrxRename(f);
                }
            },
            // INV REGISTER LN : format
            new FileHandler() {
                @Override public boolean supports(File f) {
                    return isInvRegisterLN(f);
                }
                @Override public void run(File f) throws IOException {
                    processInvRegisterLN(f);
                }
                private boolean isInvRegisterLN(File f) {
                    return f.toString().contains(ExcelConstants.INV_REGISTER_LN_SHEET);
                }
                private void processInvRegisterLN(File file) throws IOException {
                    log.info("Process InvRegisterLN file : {}", file);
                    String [] argFile = { file.toString()};
                    formatInvRegisterLN.execute(argFile);
                }
            }
    );


    @Autowired
    public DirectoryParser (CorrectionImputation correctionImputation, FormatActivity formatActivity, FormatInvRegisterLN formatInvRegisterLN, FormatTRX formatTRX) {
        this.correctionImputation = correctionImputation;
        this.formatActivity = formatActivity;
        this.formatInvRegisterLN = formatInvRegisterLN;
        this.formatTRX = formatTRX;
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


    public boolean isListFilesEmpty() {
        return listFiles == null || listFiles.length == 0;
    }


    public void processList() {
        Stream.of(listFiles)
                .forEach(f -> {
                    log.info("ProcessList file : {}", f.getName());
                    handlers.stream()
                            .filter(h -> h.supports(f))
                            .forEach(h -> {
                                try { h.run(f);
                                } catch (IOException e) {
                                    throw new FileHandlingException(h.getClass().getSimpleName(),e, -1); }
                            });
                });

    }

    private boolean isActivity(File f) { return f.toString().contains(ExcelConstants.ACTIVITY_SHEET); }
    private boolean isTrx(File f)      { return f.toString().contains(ExcelConstants.TRX_SHEET); }

    public void processFormatTRX(File file) {
        log.info("Process FormatTRX file : {}", file);
        String [] trxFile = { file.toString()};
        formatTRX.execute(trxFile);
    }
    public void processActivity(File file) throws IOException {
        log.info("Process activity file : {}", file);
        String [] activityFile = { file.toString()};
        formatActivity.execute(activityFile);
    }
    public void processActivityRename(File file) {
        log.info("Process rename activity file : {}", file);
        if (FileNameGenerator.hasFileNoPrefix(file))
            FileNameGenerator.renamePSA(file, ExcelConstants.DEFAULT_SHEET,ExcelConstants.ACTIVITY_CONTRACT_CELL);
    }
    public void processTrxRename(File file) {
        log.info("Process renameTRX file : {}", file);
        if (FileNameGenerator.hasFileNoPrefix(file))
            FileNameGenerator.renamePSA(file,ExcelConstants.DEFAULT_SHEET,ExcelConstants.TRX_CONTRACT_CELL);
    }
    public void processCorrectionImputation(File file) throws IOException {
        log.info("Process correction imputationTRX file : {}", file);
        String [] trxFile = { file.toString(),ExcelConstants.DEFAULT_SHEET};
        correctionImputation.execute(trxFile);
    }


}
