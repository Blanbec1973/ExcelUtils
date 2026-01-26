package org.heyner.excelutils.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GracefulExitExceptionTest {
    @Test
    void testGracefulExitException() {
        GracefulExitException ex = new GracefulExitException("Test", 0);
        assertEquals("Test", ex.getMessage());
        assertEquals(0, ex.getExitCode());
    }
}