package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
@EnableConfigurationProperties(CommandProperties.class)
@Slf4j
public class ArgsChecker {
    private final CommandProperties commandProperties;
    @Autowired
    public ArgsChecker(CommandProperties commandProperties) {
        this.commandProperties = commandProperties;
    }

    public boolean validate(String [] args) {
        if (log.isInfoEnabled())
            log.info("Arguments : {}", Arrays.toString(args));

        //Check argument present :
        if (args.length == 0) {
            log.error("No argument, end of program.");
            System.exit(-1);
        }

        // Check function (first argument) :
        if (!AvailableFunctions.isFunctionValid(args[0])) {
            log.error("Invalid function : {}", args[0]);
            System.exit(-1);
        }

        //Check number of argument for thr function :
        controlNumberOfArgument(args);

        return true;
    }

    private void controlNumberOfArgument(String [] args) {
        int expected = 0;
        try {
            log.info("Config : {}",commandProperties.getCommands().toString());
            expected = commandProperties.getCommands().get(args[0]).getCounterarguments();
        } catch (NumberFormatException e) {
            log.error("Unable to parse number of arguments of : {}", args[0]+".numberOfArgument");
            System.exit(-1);
        }

        if ( args.length != expected) {
            log.error("Invalid number of arguments, expected : {}, actual : {}", expected, args.length);
            System.exit(-1);
        }

    }

}
