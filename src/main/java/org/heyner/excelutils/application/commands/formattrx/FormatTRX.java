package org.heyner.excelutils.application.commands.formattrx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.FormatTRXPort;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormatTRX implements Command<FormatTRXArgs> {

    private final FormatTRXPort formatTRXPort;
    private final CliPrinter cliPrinter;

    @Override
    public String name() {
        return "formattrx";
    }

    @Override
    public FormatTRXArgs parse(String[] args) {
        validate(args);
        return new FormatTRXArgs(Path.of(args[1]));
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
    public void execute(FormatTRXArgs args) {
        formatTRXPort.deleteFirstLineContaining(args.inputFile(),
                                                ExcelConstants.DEFAULT_SHEET,
                                         "Transaction analysis");
        cliPrinter.info("SUCCESS: formatting completed");
    }
}
