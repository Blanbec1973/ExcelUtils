package org.heyner.excelutils.application.commands.analyzetrx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.ExcelTransferPort;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.application.config.AnalyzeTRXConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.util.DateTemplateExpander;
import org.heyner.excelutils.application.service.filenaming.ResultNamer;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyzeTRX implements Command<AnalyzeTRXArgs> {
    private final AnalyzeTRXConfig analyzeTRXConfig;
    private final DateTemplateExpander dateTemplateExpander;
    private final ModelCloner modelCloner;
    private final ExcelTransferPort excelTransfer;
    private final ResultNamer resultNamer;
    private final CliPrinter cliPrinter;

    private static final String TRANSFERRED_ROWS_LOG = "Number of transferred rows: {}";

    @Override
    public String name() {
        return "analyzetrx";
    }

    @Override
    public AnalyzeTRXArgs parse(String[] args) {
        validate(args);
        return new AnalyzeTRXArgs(Path.of(args[1]));
    }

    private void validate(String[] args) {
        if (args[1] == null || args[1].isBlank()) {
            throw new IllegalArgumentException("ERROR: input file is required");
        }

        Path inputFile = Path.of(args[1]);
        if (!Files.exists(inputFile)) {
            throw new IllegalArgumentException("ERROR: file not found: " + inputFile);
        }
        if (!Files.isRegularFile(inputFile)) {
            throw new IllegalArgumentException("ERROR: expected a file: " + inputFile);
        }
    }

    @Override
    public void execute(AnalyzeTRXArgs args) {
        log.info("Analyzing TRX file {}", args.inputFile());
        cliPrinter.info("Analyzing TRX file...");
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
        cliPrinter.info("Rows transferred: %d".formatted(rowCount));

        resultNamer.renameIfNeeded(pathResultFile, ExcelConstants.DATAS_SHEET, ExcelConstants.TRX_CONTRACT_CELL);
        cliPrinter.info("SUCCESS: analysis completed");
    }

}
