package org.heyner.excelutils.application.commands.lissage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.ports.LissagePort;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

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
        return new LissageArgs(
                Path.of(args[1]),
                Integer.parseInt(args[2]),
                Double.parseDouble(args[3].replace(',', '.'))
        );
    }

    @Override
    public void execute(LissageArgs args) {
        lissagePort.smooth(args.inputFile(), args.lissageRow(), args.targetMargin());
    }
}
