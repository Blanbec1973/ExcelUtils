package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FunctionalException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
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
    private final CustomExitCodeGenerator exitCodeGenerator;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (log.isInfoEnabled())
            log.info("Main begins with arguments : {}", Arrays.toString(args));

        ConfigurableApplicationContext context = SpringApplication.run(ExcelUtils.class, args);
        int exitCode = SpringApplication.exit(context);

        log.info("Program ends in {} ms.", System.currentTimeMillis()-start);
        System.exit(exitCode);

    }
    @Autowired
    public ExcelUtils(ApplicationProperties applicationProperties, List<CommandService> services,
                      ArgsChecker argsChecker,
                      CustomExitCodeGenerator exitCodeGenerator) {
        this.applicationProperties = applicationProperties;
        this.argsChecker = argsChecker;
        this.exitCodeGenerator = exitCodeGenerator;


        this.commandMap = services.stream()
                .collect(Collectors.toMap(
                        CommandService::getCommandName,
                        s -> s,
                        (s1, s2) -> { // merge function en cas de clé dupliquée
                            throw new IllegalStateException(
                                    "Duplicate command name: " + s1.getCommandName());
                        }));

                        log.debug("Available commands : {}", commandMap.keySet());
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
            CommandService service = commandMap.get(command);
            if (service == null) {
                throw new MissingConfigurationException("Unable to load command : " + command, 2);
            }
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
