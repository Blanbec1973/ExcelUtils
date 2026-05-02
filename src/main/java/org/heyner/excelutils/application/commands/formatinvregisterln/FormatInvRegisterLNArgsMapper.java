package org.heyner.excelutils.application.commands.formatinvregisterln;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FormatInvRegisterLNArgsMapper implements CommandArgsMapper {

    @Override
    public String supports() {
        return "formatinvregisterln";
    }

    @Override
    public CommandArgs map(String[] args) {
        return new FormatInvRegisterLNArgs(Path.of(args[1]));
    }
}
