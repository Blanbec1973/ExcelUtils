package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication
@ComponentScan(basePackages = "org.heyner.excelutils")
@Slf4j
public class ExcelUtils implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final ArgsChecker argsChecker;
    private final Map<String, CommandService> commandMap;

    public static void main(String[] args) {
        if (log.isInfoEnabled())
            log.info("Main begins with arguments : {}", Arrays.toString(args));

        ConfigurableApplicationContext context = null;
        int exitCode = 0;

        try {
            SpringApplication.run(ExcelUtils.class, args);
        } catch (FatalApplicationException e) {
            log.error("Fatal Error : {}", e.getMessage());
            exitCode = e.getExitCode();
        } catch (GracefulExitException e) {
            log.info("Program ends normally : {}", e.getMessage());
            exitCode = e.getExitCode();
        } finally {
            if (context != null) {
                context.close();
            }
        }
        System.exit(exitCode);
    }
    @Autowired
    public ExcelUtils(ApplicationProperties applicationProperties, List<CommandService> services, ArgsChecker argsChecker) {
        this.applicationProperties = applicationProperties;
        this.argsChecker = argsChecker;

        this.commandMap = services.stream()
                .collect(Collectors.toMap(CommandService::getCommandName, s -> s));

        log.info("Available commands : {}", commandMap.keySet());
    }

    @Override
    public void run(String... args) throws Exception {
        String projectName = applicationProperties.getProjectName();
        String version = applicationProperties.getVersion();
        log.info("Beginning : {} version {} function {}",
                projectName,
                version,
                args[0]);

        argsChecker.validate(args);
        String command = args[0].toLowerCase();
        log.info("Command : *{}*",command);
        CommandService service = commandMap.get(command);
        if (service == null) {
            throw new FatalApplicationException("Unknown command: " + command, 2);
        }
        service.execute(args);
    }
}
