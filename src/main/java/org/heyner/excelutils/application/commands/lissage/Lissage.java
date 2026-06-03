package org.heyner.excelutils.application.commands.lissage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.LissagePort;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class Lissage implements Command<LissageArgs> {

    private final LissagePort lissagePort;

    @Override
    public String name() {
        return "lissage";
    }

    @Override
    public LissageArgs parse(String[] args) {
        validate(args);
        return new LissageArgs(
                Path.of(args[1]),
                Integer.parseInt(args[2]),
                Double.parseDouble(args[3].replace(',', '.'))
        );
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

        int lissageRow;
        try {
            lissageRow = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: row must be an integer", e);
        }
        if (lissageRow < 1) {
            throw new IllegalArgumentException("ERROR: row must be greater than or equal to 1");
        }

        double targetMargin;
        try {
            targetMargin = Double.parseDouble(args[3].replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: target margin must be a decimal number", e);
        }
        if (targetMargin == 1.0) {
            throw new IllegalArgumentException("ERROR: target margin cannot be 1.0");
        }
        if (targetMargin <= 0.0 || targetMargin >= 1.0) {
            throw new IllegalArgumentException("ERROR: target margin must be between 0 and 1");
        }

    }

    @Override
    public void execute(LissageArgs args) {
        lissagePort.smooth(args.inputFile(), args.lissageRow(), args.targetMargin());
    }
}
