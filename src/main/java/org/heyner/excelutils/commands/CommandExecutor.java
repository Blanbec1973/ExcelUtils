package org.heyner.excelutils.commands;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.ExitCodes;
import org.heyner.excelutils.commands.commandParser.CommandParser;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandRegistry registry;
    private final CommandParser parser;

    public void execute(String[] args) throws Exception {
        String command = args[0].toLowerCase();

        CommandService service = registry.find(command)
                .orElseThrow(() -> new MissingConfigurationException(
                        "Unable to load command : " + command,
                        ExitCodes.MISSING_CONFIGURATION
                ));

        CommandArgs commandArgs = parser.parse(args);

        service.execute(commandArgs);
    }
}
