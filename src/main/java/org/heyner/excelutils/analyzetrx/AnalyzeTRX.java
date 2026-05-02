package org.heyner.excelutils.analyzetrx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandArgs;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.excel.ExcelTransferPort;
import org.heyner.excelutils.utils.DateTemplateExpander;
import org.heyner.excelutils.utils.filenaming.ResultNamer;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyzeTRX implements CommandService<AnalyzeTRXArgs> {
    private final AnalyzeTRXConfig analyzeTRXConfig;
    private final DateTemplateExpander dateTemplateExpander;
    private final ModelCloner modelCloner;
    private final ExcelTransferPort excelTransfer;
    private final ResultNamer resultNamer;

    private static final String BEGINNING_LOG = "Beginning {}";
    private static final String TRANSFERRED_ROWS_LOG = "Number of transferred rows: {}";

    @Override
    public String getCommandName() {
        return "analyzetrx";
    }

    @Override
    public void execute(AnalyzeTRXArgs args) {
        Path pathInput = args.inputFile();
        Path pathModel = Path.of(analyzeTRXConfig.getPathModel());
        Path pathResultFile = Path.of(dateTemplateExpander.expand(analyzeTRXConfig.getPathResultFile()));
        String sheetIn = analyzeTRXConfig.getSheetIn();
        String sheetOut = analyzeTRXConfig.getSheetOut();

        modelCloner.copy(pathModel, pathResultFile);

        int rowCount = excelTransfer.transfer(pathInput, pathResultFile, sheetIn, sheetOut);

        log.info(TRANSFERRED_ROWS_LOG, rowCount);

        resultNamer.renameIfNeeded(pathResultFile, ExcelConstants.DATAS_SHEET, ExcelConstants.TRX_CONTRACT_CELL);
    }

}
