package org.heyner.excelutils.application.commands.formatactivity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.FormatActivityPort;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormatActivity implements Command<FormatActivityArgs> {

    private final FormatActivityPort formatActivityPort;

    public void execute(FormatActivityArgs args) {
        formatActivityPort.format(args.inputFile());
    }
    @Override
    public String name() {
        return "formatactivity";
    }

    @Override
    public FormatActivityArgs parse(String[] args) {
        return new FormatActivityArgs(Path.of(args[1]));
    }
}
