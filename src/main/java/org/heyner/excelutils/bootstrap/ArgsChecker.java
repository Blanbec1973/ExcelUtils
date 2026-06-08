package org.heyner.excelutils.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.CommandSpec;
import org.heyner.excelutils.application.commands.core.CommandSpecCatalog;
import org.heyner.excelutils.shared.exitcode.ExitCodes;
import org.heyner.excelutils.shared.exception.InvalidArgumentCountException;
import org.heyner.excelutils.shared.exception.InvalidFunctionException;
import org.heyner.excelutils.shared.exception.MissingConfigurationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class ArgsChecker {
    private final CommandSpecCatalog catalog;

    private static final String ARGUMENTS_LOG = "Arguments: {}";

    public boolean validateOrThrow(String [] args) {
        if (log.isDebugEnabled())
            log.debug(ARGUMENTS_LOG, Arrays.toString(args));

        //Check argument present :
        if (args == null || args.length == 0) {
            throw new MissingConfigurationException(ExitCodes.USAGE_ERROR);
        }

        // Check function (first argument) :
        String cmd = args[0].toLowerCase(Locale.ROOT);
        if ("help".equals(cmd) || "--help".equals(cmd)) {
            return true;
        }
        CommandSpec spec = catalog.find(cmd)
                .orElseThrow(() -> new InvalidFunctionException(cmd, ExitCodes.USAGE_ERROR));

        int expected = spec.expectedArgs();
        int actual = args.length;

        if (actual != expected) {
            throw new InvalidArgumentCountException(cmd, expected, actual, ExitCodes.USAGE_ERROR);
        }
        return true;
    }
}
