package org.heyner.excelutils.application.commands.core.commandparser;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandArgsMapper;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.MissingConfigurationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultCommandParser implements CommandParser {

    private final Map<String, CommandArgsMapper> mapperMap;

    public DefaultCommandParser(List<CommandArgsMapper> mappers) {
        this.mapperMap = mappers.stream()
                .collect(Collectors.toMap(
                        m -> m.supports().toLowerCase(Locale.ROOT),
                        m -> m
                ));
    }

    @Override
    public CommandArgs parse(String[] args) {
        String command = args[0].toLowerCase(Locale.ROOT);
        CommandArgsMapper mapper = mapperMap.get(command);
        if (mapper == null) {
            throw new MissingConfigurationException(
                    "Unknown command: " + command,
                    ExitCodes.MISSING_CONFIGURATION
            );
        }
        return mapper.map(args);
    }
}