package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.exceptions.InvalidArgumentCountException;
import org.heyner.excelutils.exceptions.InvalidFunctionException;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
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
        if (log.isDebugEnabled())
            log.debug("Arguments : {}", Arrays.toString(args));

        //Check argument present :
        if (args.length == 0) {
            throw new MissingConfigurationException("No argument, end of program.", -1);
        }

        // Check function (first argument) :
        if (!AvailableFunctions.isFunctionValid(args[0])) {
            throw new InvalidFunctionException(args[0], -1);
        }

        //Check number of argument for thr function :
        controlNumberOfArgument(args);

        return true;
    }

    private void controlNumberOfArgument(String [] args) {
        int expected;
        try {
            log.debug("Config : {}",commandProperties.getCommands().toString());
            expected = commandProperties.getCommands().get(args[0]).getCounterarguments();
        } catch (NumberFormatException | NullPointerException e) {
            throw new MissingConfigurationException("Unable to parse number of arguments for function: " + args[0], -1);
        }

        if ( args.length != expected) {
            throw new InvalidArgumentCountException(expected, args.length, -1);
        }

    }

}
