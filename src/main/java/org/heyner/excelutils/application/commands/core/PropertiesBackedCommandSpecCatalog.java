package org.heyner.excelutils.application.commands.core;

import org.heyner.excelutils.infrastructure.config.CommandProperties;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.MissingConfigurationException;
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
            throw new MissingConfigurationException(
                    "Unknown command: " + name,
                    ExitCodes.MISSING_CONFIGURATION
            );
        }
        return name.toLowerCase(Locale.ROOT);
    }

    private static CommandSpec toSpec(String rawName, Integer args) {
        if (args == null || args < 1) {
            throw new MissingConfigurationException(
                "counterarguments must be >= 1 for command: " + rawName, ExitCodes.MISSING_CONFIGURATION
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
