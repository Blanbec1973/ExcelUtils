package org.heyner.excelutils.application.commands.core;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.heyner.excelutils.cli.HelpPrinter;
import org.heyner.excelutils.shared.config.ApplicationProperties;
import org.heyner.excelutils.shared.constants.ExitCodeHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@lombok.RequiredArgsConstructor
public class CommandDispatcher implements CommandLineRunner {

    private final ApplicationProperties applicationProperties;
    private final ArgsChecker argsChecker;
    private final ExitCodeHandler exitCodeHandler;
    private final CommandExecutor commandExecutor;
    private final HelpPrinter helpService;

    private static final String BEGINNING_LOG = "Starting {} {}";

    @Override
    public void run(String... args) {
        try {
            logStartup();

            if (isGlobalHelp(args)) {
                printHelp(args);
                return;
            }

            if (isCommandSpecificHelp(args)) {
                helpService.printCommand(args[0]);
                return;
            }

            argsChecker.validateOrThrow(args);
            commandExecutor.execute(args);

        } catch (Exception e) {
            exitCodeHandler.handle(e);
        }
    }

    private void logStartup() {
        String projectName = applicationProperties.getProjectName();
        String version = applicationProperties.getVersion();
        log.info(BEGINNING_LOG, projectName, version);
    }

    private boolean isGlobalHelp(String[] args) {
        return args != null
                && args.length > 0
                && ("help".equalsIgnoreCase(args[0]) || "--help".equalsIgnoreCase(args[0]));
    }

    private boolean isCommandSpecificHelp(String[] args) {
        return args != null
                && args.length == 2
                && "--help".equalsIgnoreCase(args[1])
                && !isGlobalHelp(args);
    }

    private void printHelp(String[] args) {
        if (args.length >= 2) {
            helpService.printCommand(args[1]);
        } else {
            helpService.printAll();
        }
    }
}