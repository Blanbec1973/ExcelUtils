package org.heyner.excelutils.application.commands.correctionimputation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.common.excelfile.ExcelConstants;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.application.config.CorrectionImputationConfig;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorrectionImputation implements Command<CorrectionImputationArgs> {
    private final CorrectionImputationConfig correctionImputationConfig;
    private final CorrectionImputationPort port;
    private final CliPrinter cliPrinter;

    private static final String CORRECTION_DISABLED_LOG = "CorrectionImputation is disabled by configuration. Skipping execution.";

    public void execute(CorrectionImputationArgs args) {
        if (!correctionImputationConfig.isCorrectionImputationActionEnabled()) {
            log.info(CORRECTION_DISABLED_LOG);
            return;
        }
        log.info("Beginning Timesheet correction, file to proceed: {}", args.inputFile());
        port.correct(args.inputFile(), args.sheetName());
        log.info("CorrectionImputation completed for {}", args.inputFile());
        cliPrinter.info("SUCCESS: correction completed");
    }

    @Override
    public String name() {
        return "correctionimputation";
    }

    @Override
    public CorrectionImputationArgs parse(String[] args) {
        validate(args);
        return new CorrectionImputationArgs(Path.of(args[1]), ExcelConstants.DEFAULT_SHEET);
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
}
