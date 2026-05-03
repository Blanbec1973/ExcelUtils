package org.heyner.excelutils.application.commands.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandRegistry {

    private final Map<String, Command<? extends CommandArgs>> commands;

    public CommandRegistry(List<Command<?>> commands) {
        this.commands = commands.stream()
                .collect(Collectors.toMap(
                        cmd -> normalize(cmd.name()),
                        cmd -> cmd,
                        (left, right) -> {
                            throw new IllegalStateException(
                                    "Duplicate command registered: " + left.name()
                            );
                        }
                ));

        log.debug("Available commands: {}", getCommandNames());
    }

    public Optional<Command<? extends CommandArgs>> find(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(commands.get(normalize(name)));
    }

    public List<String> getCommandNames() {
        return commands.keySet().stream().sorted().toList();
    }

    private static String normalize(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}