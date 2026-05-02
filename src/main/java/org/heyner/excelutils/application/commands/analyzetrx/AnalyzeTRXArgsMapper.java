package org.heyner.excelutils.application.commands.analyzetrx;

import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class AnalyzeTRXArgsMapper implements CommandArgsMapper {

    @Override
    public String supports() {
        return "analyzetrx";
    }

    @Override
    public CommandArgs map(String[] args) {
        return new AnalyzeTRXArgs(Path.of(args[1]));
    }
}
