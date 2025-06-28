package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

        SpringApplication.run(ExcelUtils.class,args);

        if (args.length == 0) {
            log.info("Aucun argument fourni. Lancement en mode test ou défaut.");
        }
    }
    @Autowired
    public ExcelUtils(ApplicationProperties applicationProperties, List<CommandService> services, ArgsChecker argsChecker) {
        this.applicationProperties = applicationProperties;
        this.argsChecker = argsChecker;
        ZipSecureFile.setMinInflateRatio(0.001);
        IOUtils.setByteArrayMaxOverride(200000000);

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
        service.execute(args);
    }
}
