package org.heyner.excelutils.shared.exitcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExitCodeGeneratorTest {
    @Test
    void testExitCodeSetterGetter() {
        CustomExitCodeGenerator generator = new CustomExitCodeGenerator();
        generator.setExitCode(5);
        assertEquals(5, generator.getExitCode());
    }
}