package org.heyner.excelutils.cli;



import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Slf4jCliPrinterTest {

    @Test
    void printer_logs_messages_with_expected_levels() {
        Logger logger = (Logger) LogManager.getLogger("org.heyner.excelutils.cli");
        List<LogEvent> events = new ArrayList<>();
        AbstractAppender appender = new AbstractAppender("TestCliAppender", null,
                PatternLayout.createDefaultLayout(), false, Property.EMPTY_ARRAY) {
            @Override
            public void append(LogEvent event) {
                events.add(event.toImmutable());
            }
        };

        Level originalLevel = logger.getLevel();
        appender.start();
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);

        try {
            Slf4jCliPrinter printer = new Slf4jCliPrinter();

            printer.info("hello");
            printer.warn("watch-out");
            printer.error("boom");
            printer.blankLine();

            assertEquals(4, events.size());

            assertEquals(Level.INFO, events.get(0).getLevel());
            assertEquals("hello", events.get(0).getMessage().getFormattedMessage());

            assertEquals(Level.WARN, events.get(1).getLevel());
            assertEquals("watch-out", events.get(1).getMessage().getFormattedMessage());

            assertEquals(Level.ERROR, events.get(2).getLevel());
            assertEquals("boom", events.get(2).getMessage().getFormattedMessage());

            assertEquals(Level.INFO, events.get(3).getLevel());
            assertEquals("", events.get(3).getMessage().getFormattedMessage());
        } finally {
            logger.removeAppender(appender);
            logger.setLevel(originalLevel);
            appender.stop();
        }
    }
}

