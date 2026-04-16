package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.heyner.excelutils.exitcode.ExitCodeHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@lombok.RequiredArgsConstructor
public class CommandDispatcher implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final ArgsChecker argsChecker;
    private final CommandRegistry registry;
    private final ExitCodeHandler exitCodeHandler;


    @Override
    public void run(String... args) {
        try {
            logStartup();
            argsChecker.validateOrThrow(args);

            String command = extractCommand(args);
            CommandService service = findCommandService(command);

            service.execute(args);
        } catch (Exception e) {
            exitCodeHandler.handle(e);
        }
    }

    private void logStartup() {
        String projectName = applicationProperties.getProjectName();
        String version = applicationProperties.getVersion();
        log.info("Beginning : {} version {}", projectName, version);
    }

    private String extractCommand(String[] args) {
        String command = args[0].toLowerCase();
        log.debug("Command : *{}*", command);
        return command;
    }

    private CommandService findCommandService(String command) {
        return registry.find(command)
                .orElseThrow(() -> new MissingConfigurationException("Unable to load command : " + command, ExitCodes.MISSING_CONFIGURATION));
    }
}
