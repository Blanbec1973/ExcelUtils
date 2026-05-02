package org.heyner.excelutils.application.commands.fusiontrx;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FusionTRXArgsMapper implements CommandArgsMapper {

    @Override
    public String supports() {
        return "fusiontrx";
    }

    @Override
    public CommandArgs map(String[] args) {
        return new FusionTRXArgs(Path.of(args[1]), Path.of(args[2]));
    }
}
