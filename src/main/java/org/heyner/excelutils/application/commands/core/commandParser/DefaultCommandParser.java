package org.heyner.excelutils.application.commands.core.commandParser;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandRegistry;
import org.heyner.excelutils.application.commands.analyzetrx.AnalyzeTRXArgs;
import org.heyner.excelutils.application.commands.directoryparser.DirectoryParserArgs;
import org.heyner.excelutils.application.commands.format_trx.FormatTRXArgs;
import org.heyner.excelutils.application.commands.formatactivity.FormatActivityArgs;
import org.heyner.excelutils.application.commands.formatinvregisterln.FormatInvRegisterLNArgs;
import org.heyner.excelutils.application.commands.fusiontrx.FusionTRXArgs;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class DefaultCommandParser implements CommandParser {

    private final CommandRegistry registry;

    @Override
    public CommandArgs parse(String[] args) {
        String command = args[0].toLowerCase();

        return switch (command) {
            case "analyzetrx" -> new AnalyzeTRXArgs(
                    Path.of(args[1])
            );
            case "formattrx" -> new FormatTRXArgs(
                    Path.of(args[1])
            );
            case "formatactivity" -> new FormatActivityArgs(
                    Path.of(args[1])
            );
            case "formatinvregisterln" -> new FormatInvRegisterLNArgs(
                    Path.of(args[1])
            );
            case "directoryparser" -> new DirectoryParserArgs(
                    Path.of(args[1])
            );
            case "fusiontrx" -> new FusionTRXArgs(
                    Path.of(args[1]),
                    Path.of(args[2])
            );
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
}