package org.heyner.excelutils.application.commands.core;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.CatalogConfigurationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandRegistry registry;

    public void execute(String[] args) {
        String commandName = args[0];

        Command<? extends CommandArgs> command = registry.find(commandName)
                .orElseThrow(() -> new CatalogConfigurationException(
                        "Command not registered: " + commandName,
                        ExitCodes.CONFIG_ERROR
                ));

        executeCommand(command, args);
    }

    private <T extends CommandArgs> void executeCommand(Command<T> command, String[] args) {
        T parsed = command.parse(args);
        command.execute(parsed);
    }
}