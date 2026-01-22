package org.heyner.excelutils.commands;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

/**
 * Adapte CommandProperties vers un CommandSpecCatalog immuable.
 * - Normalise les noms de commandes en lowercase (clé du catalogue)
 * - Valide et fige la map au démarrage (fail-fast si conf invalide)
 */
@Component
@Slf4j
public class PropertiesBackedCommandSpecCatalog implements CommandSpecCatalog {

    private final Map<String, CommandSpec> catalog;

    @Autowired
    public PropertiesBackedCommandSpecCatalog(CommandProperties props) {
        // 1) Lire la map
        Map<String, CommandProperties.CommandConfig> configured = props.getCommands();

        // 2) Valider (fail-fast) + normaliser + figer
        this.catalog = Map.copyOf(
                configured.entrySet().stream().collect(toMap(
                        e -> normalize(e.getKey()),
                        e -> toSpec(e.getKey(), e.getValue().getCounterarguments())
                ))
        );
    }

    @PostConstruct
    public void postConstrucDisplay() {
        log.debug(catalog.toString());
    }


    private static String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Command name must not be blank in configuration");
        }
        return name.toLowerCase(Locale.ROOT);
    }

    private static CommandSpec toSpec(String rawName, Integer expectedArgs) {
        if (expectedArgs == null) {
            throw new IllegalArgumentException("Missing expectedArgs for command: " + rawName);
        }
        if (expectedArgs < 1) {
            throw new IllegalArgumentException("expectedArgs must be >= 1 for command: " + rawName);
        }
        return new CommandSpec(rawName, expectedArgs);
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
