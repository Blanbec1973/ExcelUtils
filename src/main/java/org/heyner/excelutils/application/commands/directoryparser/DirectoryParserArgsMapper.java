package org.heyner.excelutils.application.commands.directoryparser;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class DirectoryParserArgsMapper implements CommandArgsMapper {

    @Override
    public String supports() {
        return "directoryparser";
    }

    @Override
    public CommandArgs map(String[] args) {
        return new DirectoryParserArgs(Path.of(args[1]));
    }
}