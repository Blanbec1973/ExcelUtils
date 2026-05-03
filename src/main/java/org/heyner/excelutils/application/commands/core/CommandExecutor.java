package org.heyner.excelutils.application.commands.core;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.MissingConfigurationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandRegistry registry;

    public void execute(String[] args) throws Exception {
        String commandName = args[0];

        Command command = registry.find(commandName);

        if (command == null) {
            throw  new MissingConfigurationException(
                    "Unable to load command : " + commandName,
                    ExitCodes.CONFIG_ERROR
            );
        }

        CommandArgs parsed = command.parse(args);

        command.execute(parsed);
    }
}
