package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.heyner.excelutils.exitcode.ExitCodeHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommandDispatcher implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final ArgsChecker argsChecker;
    private final CommandRegistry registry;
    private final ExitCodeHandler exitCodeHandler;

    public CommandDispatcher(ApplicationProperties applicationProperties,
                             ArgsChecker argsChecker,
                             CommandRegistry commandRegistry, ExitCodeHandler exitCodeHandler) {
        this.applicationProperties = applicationProperties;
        this.argsChecker = argsChecker;
        this.registry = commandRegistry;
        this.exitCodeHandler = exitCodeHandler;

    }

    @Override
    public void run(String... args) {
        try {
            String projectName = applicationProperties.getProjectName();

            String version = applicationProperties.getVersion();
            log.info("Beginning : {} version {}",
                    projectName,
                    version);

            argsChecker.validateOrThrow(args);
            String command = args[0].toLowerCase();
            log.debug("Command : *{}*", command);

            CommandService service = registry.find(command)
                    .orElseThrow(() -> new MissingConfigurationException("Unable to load command : " + command, 2));

            service.execute(args);
        } catch (Throwable t) {
            exitCodeHandler.handle(t);
        }
    }
}
