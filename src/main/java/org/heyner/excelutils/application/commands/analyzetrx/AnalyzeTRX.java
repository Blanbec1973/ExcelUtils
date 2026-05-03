package org.heyner.excelutils.application.commands.analyzetrx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.ExcelTransferPort;
import org.heyner.excelutils.infrastructure.config.AnalyzeTRXConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.utils.DateTemplateExpander;
import org.heyner.excelutils.shared.utils.filenaming.ResultNamer;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyzeTRX implements Command<AnalyzeTRXArgs> {
    private final AnalyzeTRXConfig analyzeTRXConfig;
    private final DateTemplateExpander dateTemplateExpander;
    private final ModelCloner modelCloner;
    private final ExcelTransferPort excelTransfer;
    private final ResultNamer resultNamer;

    private static final String TRANSFERRED_ROWS_LOG = "Number of transferred rows: {}";

    @Override
    public String name() {
        return "analyzetrx";
    }

    @Override
    public AnalyzeTRXArgs parse(String[] args) {
        return new AnalyzeTRXArgs(Path.of(args[1]));
    }

    @Override
    public void execute(AnalyzeTRXArgs args) {
        log.info("Analyzing TRX file {}", args.inputFile());
        Path pathInput = args.inputFile();
        Path pathModel = Path.of(analyzeTRXConfig.getPathModel());
        log.debug("Model file: {}", pathModel);
        Path pathResultFile = Path.of(dateTemplateExpander.expand(analyzeTRXConfig.getPathResultFile()));
        log.debug("Result file before expansion: {}", analyzeTRXConfig.getPathResultFile());
        String sheetIn = analyzeTRXConfig.getSheetIn();
        String sheetOut = analyzeTRXConfig.getSheetOut();

        modelCloner.copy(pathModel, pathResultFile);

        int rowCount = excelTransfer.transfer(pathInput, pathResultFile, sheetIn, sheetOut);

        log.info(TRANSFERRED_ROWS_LOG, rowCount);

        resultNamer.renameIfNeeded(pathResultFile, ExcelConstants.DATAS_SHEET, ExcelConstants.TRX_CONTRACT_CELL);
        log.info("TRX analysis completed successfully for {}", args.inputFile());
    }

}
