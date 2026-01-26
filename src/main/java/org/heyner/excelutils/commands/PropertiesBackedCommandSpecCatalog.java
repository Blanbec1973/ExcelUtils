package org.heyner.excelutils.commands;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class PropertiesBackedCommandSpecCatalog implements CommandSpecCatalog {

    private final Map<String, CommandSpec> catalog;

    public PropertiesBackedCommandSpecCatalog(CommandProperties props) {
        this.catalog = Map.copyOf(
            props.getCounterarguments().entrySet().stream()
                .collect(Collectors.toMap(
                    e -> normalize(e.getKey()),
                    e -> toSpec(e.getKey(), e.getValue())
                ))
        );
    }

    private static String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Command name must not be blank");
        }
        return name.toLowerCase(Locale.ROOT);
    }

    private static CommandSpec toSpec(String rawName, Integer args) {
        if (args == null || args < 1) {
            throw new IllegalArgumentException(
                "counterarguments must be >= 1 for command: " + rawName
            );
        }
        return new CommandSpec(rawName, args);
    }

    @Override
    public Optional<CommandSpec> find(String commandName) {
        if (commandName == null) return Optional.empty();
        return Optional.ofNullable(catalog.get(normalize(commandName)));
    }

    @Override
    public Set<String> names() {
        return catalog.keySet();
    }
}

