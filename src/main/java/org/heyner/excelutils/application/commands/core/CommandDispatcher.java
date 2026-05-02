package org.heyner.excelutils.application.commands.core;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.bootstrap.ArgsChecker;
import org.heyner.excelutils.shared.constants.ExitCodeHandler;
import org.heyner.excelutils.shared.config.ApplicationProperties;
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

    private static final String BEGINNING_LOG = "Beginning: {} version {}";

    @Override
    public void run(String... args) {
        try {
            logStartup();
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

}
