package org.heyner.excelutils.application.commands.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandRegistry {
    private final Map<String, Command<? extends CommandArgs>> commands;


    public CommandRegistry(List<Command<?>> commands) {
        this.commands = commands.stream()
                .collect(Collectors.toMap(
                        cmd -> cmd.name().toLowerCase(),
                        cmd -> cmd));

        log.debug("Available commands : {}", this.getCommandNames());
    }


    /** Encapsulated lookup, to avoid exposing the map */
    public Command<? extends CommandArgs> find(String name) {
        return commands.get(name.toLowerCase());
    }

    public List<String> getCommandNames() {
        return commands.keySet().stream().sorted().toList();
    }

}
