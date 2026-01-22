package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.commands.CommandSpec;
import org.heyner.excelutils.commands.CommandSpecCatalog;
import org.heyner.excelutils.exceptions.InvalidArgumentCountException;
import org.heyner.excelutils.exceptions.InvalidFunctionException;
import org.heyner.excelutils.exceptions.MissingConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
@Slf4j
public class ArgsChecker {
    private final CommandSpecCatalog catalog;
    @Autowired
    public ArgsChecker(CommandSpecCatalog catalog) {
        this.catalog = catalog;
    }

    public boolean validateOrThrow(String [] args) {
        if (log.isDebugEnabled())
            log.debug("Arguments : {}", Arrays.toString(args));

        //Check argument present :
        if (args == null || args.length == 0) {
            throw new MissingConfigurationException("No argument, end of program.", -1);
        }

        // Check function (first argument) :
        String cmd = args[0].toLowerCase();
        CommandSpec spec = catalog.find(cmd)
                .orElseThrow(() -> new InvalidFunctionException("Unknown function: " + cmd, -1));

        int expected = spec.expectedArgs();
        int actual = args.length;

        if (actual != expected) {
            throw new InvalidArgumentCountException(expected, actual, -1);
        }
        return true;
    }
}
