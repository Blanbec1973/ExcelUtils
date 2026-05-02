package org.heyner.excelutils.application.commands.formatactivity;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FormatActivityArgsMapper implements CommandArgsMapper {
    @Override
    public String supports() {
        return "formatactivity";
    }

    @Override
    public CommandArgs map(String[] args) {
        return new FormatActivityArgs(Path.of(args[1]));
    }
}
