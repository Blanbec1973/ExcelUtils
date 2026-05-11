package org.heyner.excelutils.application.commands.format_trx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.FormatTRXPort;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormatTRX implements Command<FormatTRXArgs> {

    private final FormatTRXPort formatTRXPort;

    @Override
    public String name() {
        return "formattrx";
    }

    @Override
    public FormatTRXArgs parse(String[] args) {
        return new FormatTRXArgs(Path.of(args[1]));
    }

    @Override
    public void execute(FormatTRXArgs args) {
        formatTRXPort.deleteFirstLineContaining(args.inputFile(),
                                                ExcelConstants.DEFAULT_SHEET,
                                         "Transaction analysis");
    }
}
