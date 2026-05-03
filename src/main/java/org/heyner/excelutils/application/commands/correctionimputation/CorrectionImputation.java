package org.heyner.excelutils.application.commands.correctionimputation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelConstants;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.heyner.excelutils.infrastructure.config.CorrectionImputationConfig;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorrectionImputation implements Command<CorrectionImputationArgs> {
    private final CorrectionImputationConfig correctionImputationConfig;
    private final CorrectionImputationPort port;

    private static final String CORRECTION_DISABLED_LOG = "CorrectionImputation is disabled by configuration. Skipping execution.";

    public void execute(CorrectionImputationArgs args) {
        if (!correctionImputationConfig.isCorrectionImputationActionEnabled()) {
            log.info(CORRECTION_DISABLED_LOG);
            return;
        }
        log.info("Beginning Timesheet correction, file to proceed: {}", args.inputFile());
        port.correct(args.inputFile(), args.sheetName());
        log.info("CorrectionImputation completed for {}", args.inputFile());
//        try (ExcelFile fichierExcel = ExcelFile.open(args.inputFile().toString())){

//
//            int rowNum = 0;
//            Sheet dataSheet = fichierExcel.getWorkBook().getSheet(args.sheetName());
//            fichierExcel.getWorkBook().setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//            //Get iterator to all the rows in current sheet Iterator<Row> rowIterator = dataSheet.iterator()
//            for (Row row : dataSheet) {
//               Cell cell = row.getCell(56);
//               if (cell != null && cell.getCellType() == CellType.STRING) {
//                   log.info(PROCESSING_ROW_LOG, rowNum, cell.getStringCellValue());
//                   service.processRow(row, fichierExcel);
//                }
//
//                rowNum = rowNum + 1;
//            }
//            fichierExcel.writeFichierExcel();
//        } catch (IOException e) {
//            log.error(ERROR_PROCESSING_FILE_LOG, args.inputFile(), e);
//            throw new FatalApplicationException("Error processing file: " + args.inputFile(),
//                    e, ExitCodes.FILE_PROCESSING_ERROR);
//        }
    }

    @Override
    public String name() {
        return "correctionimputation";
    }

    @Override
    public CorrectionImputationArgs parse(String[] args) {
        return new CorrectionImputationArgs(Path.of(args[1]), ExcelConstants.DEFAULT_SHEET);
    }
}
