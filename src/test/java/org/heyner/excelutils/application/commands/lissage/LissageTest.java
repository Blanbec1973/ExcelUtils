package org.heyner.excelutils.application.commands.lissage;

import org.heyner.excelutils.application.ports.LissagePort;
import org.heyner.excelutils.cli.Slf4jCliPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LissageTest {

    @TempDir
    Path tempDir;

    @Test
    void parse_reads_all_arguments() throws Exception {
        Path inputFile = tempDir.resolve("input.xlsx");
        Files.createFile(inputFile);

        LissagePort port = mock(LissagePort.class);
        Lissage lissage = new Lissage(port, mock(Slf4jCliPrinter.class));

        LissageArgs args = lissage.parse(new String[]{"lissage", inputFile.toString(), "12", "0,30"});

        assertEquals(inputFile, args.inputFile());
        assertEquals(12, args.lissageRow());
        assertEquals(0.30d, args.targetMargin(), 1.0e-9);
    }

    @Test
    void execute_delegates_to_port() {
        LissagePort port = mock(LissagePort.class);
        Lissage lissage = new Lissage(port, mock(Slf4jCliPrinter.class));
        Path inputFile = Path.of("input.xlsx");
        LissageArgs args = new LissageArgs(inputFile, 7, 0.15d);

        lissage.execute(args);

        verify(port).smooth(inputFile, 7, 0.15d);
    }
}

