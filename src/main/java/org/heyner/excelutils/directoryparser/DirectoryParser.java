package org.heyner.excelutils.directoryparser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.FileNameGenerator;
import org.heyner.excelutils.correctionimputation.CorrectionImputation;
import org.heyner.excelutils.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.exceptions.FileHandlingException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.format_trx.FormatTRX;
import org.heyner.excelutils.formatactivity.FormatActivity;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryParser implements CommandService {
    private final List<FileProcessor> processors;
    private final DirectoryLister lister;
    private final CorrectionImputation correctionImputation;
    private final FormatInvRegisterLN formatInvRegisterLN;
    private final FormatTRX formatTRX;
    private File[] listFiles;

    private final List<FileHandler> handlers = List.of(
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
        listFiles = lister.listXlsx(directoryToProcess);

        if (isListFilesEmpty()) {
            throw new GracefulExitException("No file to process in " + directoryToProcess, 0);
        }
        processList();
    }


    public boolean isListFilesEmpty() {
        return listFiles == null || listFiles.length == 0;
    }

    public void processList() {
        for (File f : listFiles) {
            processFile(f);
        }
    }

    private void processFile(File file) {
        log.info("ProcessList file : {}", file.getName());

        boolean processedByNewChain = processWithProcessors(file);

        if (!processedByNewChain) {
            processWithLegacyHandlers(file);
        }
    }

    private boolean processWithProcessors(File file) {
        boolean processed = false;

        for (FileProcessor processor : processors) {
            if (processor.supports(file)) {
                try {
                    processor.process(file);
                    processed = true;
                } catch (IOException e) {
                    throw new FileHandlingException(
                            processor.getClass().getSimpleName(),
                            e,
                            -1
                    );
                }
            }
        }
        return processed;
    }

    private void processWithLegacyHandlers(File file) {
        for (FileHandler handler : handlers) {
            if (handler.supports(file)) {
                try {
                    handler.run(file);
                } catch (IOException e) {
                    throw new FileHandlingException(
                            handler.getClass().getSimpleName(), e, -1
                    );

                }
            }
        }
    }

    private boolean isTrx(File f)      { return f.toString().contains(ExcelConstants.TRX_SHEET); }

    public void processFormatTRX(File file) {
        log.info("Process FormatTRX file : {}", file);
        String [] trxFile = { file.toString()};
        formatTRX.execute(trxFile);
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
