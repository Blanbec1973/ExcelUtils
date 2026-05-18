package org.heyner.excelutils.application.commands.lissage;

import org.heyner.excelutils.application.ports.LissagePort;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LissageTest {

    @Test
    void parse_reads_all_arguments() {
        LissagePort port = mock(LissagePort.class);
        Lissage lissage = new Lissage(port);

        LissageArgs args = lissage.parse(new String[]{"lissage", "input.xlsx", "12", "0,30"});

        assertEquals(Path.of("input.xlsx"), args.inputFile());
        assertEquals(12, args.lissageRow());
        assertEquals(0.30d, args.targetMargin(), 1.0e-9);
    }

    @Test
    void execute_delegates_to_port() {
        LissagePort port = mock(LissagePort.class);
        Lissage lissage = new Lissage(port);
        LissageArgs args = new LissageArgs(Path.of("input.xlsx"), 7, 0.15d);

        lissage.execute(args);

        verify(port).smooth(Path.of("input.xlsx"), 7, 0.15d);
    }
}

