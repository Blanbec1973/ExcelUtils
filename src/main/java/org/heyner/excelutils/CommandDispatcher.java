package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FunctionalException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@EnableConfigurationProperties(ApplicationProperties.class)
public class CommandDispatcher implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final ArgsChecker argsChecker;
    private final CommandRegistry registry;
    private final CustomExitCodeGenerator exitCodeGenerator;

    @Autowired
    public CommandDispatcher(ApplicationProperties applicationProperties, List<CommandService> services,
                             ArgsChecker argsChecker,
                             CommandRegistry commandRegistry, CustomExitCodeGenerator exitCodeGenerator) {
        this.applicationProperties = applicationProperties;
        this.argsChecker = argsChecker;
        this.registry = commandRegistry;
        this.exitCodeGenerator = exitCodeGenerator;

    }

    @Override
    public void run(String... args) {
        try {
            String projectName = applicationProperties.getProjectName();

            String version = applicationProperties.getVersion();
            log.info("Beginning : {} version {} function {}",
                    projectName,
                    version,
                    args[0]);

            argsChecker.validate(args);
            String command = args[0].toLowerCase();
            log.debug("Command : *{}*", command);

            CommandService service = registry.find(command)
                    .orElseThrow(() -> new MissingConfigurationException("Unable to load command : " + command, 2));

            service.execute(args);
        } catch (GracefulExitException e) {
            log.info("Program ends normally : {}", e.getMessage());
            exitCodeGenerator.setExitCode(e.getExitCode());
        } catch (FunctionalException e) {
            log.error(e.getMessage());
            exitCodeGenerator.setExitCode(e.getExitCode());
        }
        catch (FatalApplicationException e) {
            log.error("Fatal Error", e);
            exitCodeGenerator.setExitCode(e.getExitCode());
        } catch (Exception e) {
            log.error("Unexpected error", e);
            exitCodeGenerator.setExitCode(1);
        }
    }
}
