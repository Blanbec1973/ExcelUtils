package org.heyner.excelutils.application.commands.formatactivity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.FormatActivityPort;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;

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
        validate(args);
        return new FormatActivityArgs(Path.of(args[1]));
    }

    private void validate(String[] args) {
        if (args[1] == null || args[1].isBlank()) {
            throw new IllegalArgumentException("Input file path must not be blank");
        }

        Path inputFile = Path.of(args[1]);
        if (!Files.exists(inputFile)) {
            throw new IllegalArgumentException("Input file does not exist: " + inputFile);
        }
        if (!Files.isRegularFile(inputFile)) {
            throw new IllegalArgumentException("Input path is not a file: " + inputFile);
        }
    }
}
