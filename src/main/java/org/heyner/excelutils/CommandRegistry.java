package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandRegistry {
    private final Map<String, CommandService> commandMap;

    @Autowired
    public CommandRegistry(List<CommandService> services) {

        this.commandMap = Map.copyOf(services.stream()
                .collect(Collectors.toMap(
                        CommandService::getCommandName,
                        s -> s,
                        (s1, s2) -> { // merge function if duplicate
                            throw new IllegalStateException(
                                    "Duplicate command name: " + s1.getCommandName());
                        })));

        log.debug("Available commands : {}", commandMap.keySet());
    }


    /** Lookup encapsulé, pour éviter d’exposer la map */
    public Optional<CommandService> find(String name) {
        return Optional.ofNullable(commandMap.get(name.toLowerCase()));
    }

    /** Optionnel : utile pour messages d’aide/logs */
    public List<String> getCommandNames() {
        return commandMap.keySet().stream().sorted().toList();
    }

}
