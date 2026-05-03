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
