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
            throw new IllegalArgumentException("Input file path must not be blank");
        }

        Path inputFile = Path.of(args[1]);
        if (!Files.exists(inputFile)) {
            throw new IllegalArgumentException("Input file does not exist: " + inputFile);
        }
        if (!Files.isRegularFile(inputFile)) {
            throw new IllegalArgumentException("Input path is not a file: " + inputFile);
        }

        int lissageRow;
        try {
            lissageRow = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Lissage row must be an integer", e);
        }
        if (lissageRow < 1) {
            throw new IllegalArgumentException("Lissage row must be >= 1");
        }

        double targetMargin;
        try {
            targetMargin = Double.parseDouble(args[3].replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Target margin must be a decimal number", e);
        }
        if (targetMargin <= 0.0 || targetMargin >= 1.0) {
            throw new IllegalArgumentException("Target margin must be > 0 and < 1");
        }
    }

    @Override
    public void execute(LissageArgs args) {
        lissagePort.smooth(args.inputFile(), args.lissageRow(), args.targetMargin());
    }
}
