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
            throw new FatalApplicationException("No argument, end of program.", -1);
        }

        // Check function (first argument) :
        if (!AvailableFunctions.isFunctionValid(args[0])) {
            throw new FatalApplicationException("Invalid function: " + args[0], -1);
        }

        //Check number of argument for thr function :
        controlNumberOfArgument(args);

        return true;
    }

    private void controlNumberOfArgument(String [] args) {
        int expected;
        try {
            log.info("Config : {}",commandProperties.getCommands().toString());
            expected = commandProperties.getCommands().get(args[0]).getCounterarguments();
        } catch (NumberFormatException | NullPointerException e) {
            throw new FatalApplicationException("Unable to parse number of arguments for function: " + args[0], -1);
        }

        if ( args.length != expected) {
            throw new FatalApplicationException("Invalid number of arguments, expected: " + expected +
                                                ", actual: " + args.length, -1);
        }

    }

}
