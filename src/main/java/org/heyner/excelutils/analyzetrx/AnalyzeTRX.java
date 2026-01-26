package org.heyner.excelutils.analyzetrx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.utils.DateTemplateExpander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
@Slf4j
public class AnalyzeTRX implements CommandService {
    private final AnalyzeTRXConfig analyzeTRXConfig;
    private final DateTemplateExpander dateTemplateExpander;
    private final ModelCloner modelCloner;
    private final TrxDataTransfer trxDataTransfer;
    private final ResultNamer resultNamer;

    @Autowired
    public AnalyzeTRX(AnalyzeTRXConfig analyzeTRXConfig,
                      DateTemplateExpander dateTemplateExpander,
                      ModelCloner modelCloner, TrxDataTransfer trxDataTransfer, ResultNamer resultNamer) {
        this.analyzeTRXConfig = analyzeTRXConfig;
        this.dateTemplateExpander = dateTemplateExpander;
        this.modelCloner = modelCloner;
        this.trxDataTransfer = trxDataTransfer;
        this.resultNamer = resultNamer;
    }

    @Override
    public String getCommandName() {
        return "analyzetrx";
    }

    @Override
    public void execute(String... args) {
        log.debug("Beginning {}",args[0]);
        String pathInput = args[1];
        String pathModel = analyzeTRXConfig.getPathModel();
        String pathResultFile = dateTemplateExpander.expand(analyzeTRXConfig.getPathResultFile());
        String sheetIn = analyzeTRXConfig.getSheetIn();
        String sheetOut = analyzeTRXConfig.getSheetOut();

        modelCloner.copy(Paths.get(pathModel), Paths.get(pathResultFile));

        int rowCount = trxDataTransfer.transfer(pathInput, pathResultFile, sheetIn, sheetOut);

        log.info("Number of transferred rows : {}", rowCount);

        resultNamer.renameIfNeeded(pathResultFile, ExcelConstants.DATAS_SHEET, ExcelConstants.TRX_CONTRACT_CELL);
    }
}
