package org.heyner.excelutils.cli;



import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Slf4jCliPrinterTest {

    private Logger logger;
    private List<LogEvent> events;
    private AbstractAppender appender;
    private Level originalLevel;
    private Slf4jCliPrinter printer;

    @BeforeEach
    void setUp() {
        logger = (Logger) LogManager.getLogger("org.heyner.excelutils.cli");
        events = new ArrayList<>();
        appender = new AbstractAppender("TestCliAppender", null,
                PatternLayout.createDefaultLayout(), false, Property.EMPTY_ARRAY) {
            @Override
            public void append(LogEvent event) {
                events.add(event.toImmutable());
            }
        };
        originalLevel = logger.getLevel();
        appender.start();
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);
        printer = new Slf4jCliPrinter();
    }

    @AfterEach
    void tearDown() {
        logger.removeAppender(appender);
        logger.setLevel(originalLevel);
        appender.stop();
    }

    @Test
    void printer_logs_messages_with_expected_levels() {
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
    }

    @Test
    void infoLogsAtInfoLevel() {
        printer.info("information");

        assertEquals(1, events.size());
        assertEquals(Level.INFO, events.getFirst().getLevel());
        assertEquals("information", events.getFirst().getMessage().getFormattedMessage());
    }

    @Test
    void warnLogsAtWarnLevel() {
        printer.warn("warning");

        assertEquals(1, events.size());
        assertEquals(Level.WARN, events.getFirst().getLevel());
        assertEquals("warning", events.getFirst().getMessage().getFormattedMessage());
    }

    @Test
    void errorLogsAtErrorLevel() {
        printer.error("failure");

        assertEquals(1, events.size());
        assertEquals(Level.ERROR, events.getFirst().getLevel());
        assertEquals("failure", events.getFirst().getMessage().getFormattedMessage());
    }

    @Test
    void blankLineLogsEmptyStringAtInfoLevel() {
        printer.blankLine();

        assertEquals(1, events.size());
        assertEquals(Level.INFO, events.getFirst().getLevel());
        assertEquals("", events.getFirst().getMessage().getFormattedMessage());
    }

    @Test
    void infoLogsExactMessageContent() {
        printer.info("SUCCESS: operation completed");

        assertEquals("SUCCESS: operation completed", events.getFirst().getMessage().getFormattedMessage());
    }
}

