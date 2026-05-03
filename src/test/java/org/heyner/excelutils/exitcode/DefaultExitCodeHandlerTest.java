package org.heyner.excelutils.exitcode;

import org.heyner.excelutils.shared.constants.CustomExitCodeGenerator;
import org.heyner.excelutils.shared.constants.DefaultExitCodeHandler;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.heyner.excelutils.shared.exceptions.FunctionalException;
import org.heyner.excelutils.shared.exceptions.GracefulExitException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultExitCodeHandlerTest {

    @Test
    void shouldHandleGracefulExit() {
        CustomExitCodeGenerator gen = new CustomExitCodeGenerator();
        DefaultExitCodeHandler handler = new DefaultExitCodeHandler(gen);

        handler.handle(new GracefulExitException("ok", 0));

        assertEquals(0, gen.getExitCode());
    }

    @Test
    void shouldHandleFunctionalException() {
        CustomExitCodeGenerator gen = new CustomExitCodeGenerator();
        DefaultExitCodeHandler handler = new DefaultExitCodeHandler(gen);

        FunctionalException fe = new FunctionalException("functional", 7) {};
        handler.handle(fe);

        assertEquals(7, gen.getExitCode());
    }

    @Test
    void shouldHandleFatalException() {
        CustomExitCodeGenerator gen = new CustomExitCodeGenerator();
        DefaultExitCodeHandler handler = new DefaultExitCodeHandler(gen);

        handler.handle(new FatalApplicationException("fatal", null, 9));

        assertEquals(9, gen.getExitCode());
    }

    @Test
    void shouldHandleUnexpectedException() {
        CustomExitCodeGenerator gen = new CustomExitCodeGenerator();
        DefaultExitCodeHandler handler = new DefaultExitCodeHandler(gen);

        handler.handle(new RuntimeException("boom"));

        assertEquals(1, gen.getExitCode());
    }
}
