package org.heyner.excelutils.cli;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Slf4jCliPrinter implements CliPrinter {

    private static final Logger cliLog = LoggerFactory.getLogger("org.heyner.excelutils.cli");

    @Override
    public void info(String message) {
        cliLog.info(message);
    }

    @Override
    public void warn(String message) {
        cliLog.warn(message);
    }

    @Override
    public void error(String message) {
        cliLog.error(message);
    }

    @Override
    public void blankLine() {
        cliLog.info("");
    }
}
