package org.heyner.excelutils.application.commands.format_trx;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FormatTRXArgsMapper implements CommandArgsMapper {

    @Override
    public String supports() {
        return "formattrx";
    }

    @Override
    public CommandArgs map(String[] args) {
        return new FormatTRXArgs(Path.of(args[1]));
    }
}
