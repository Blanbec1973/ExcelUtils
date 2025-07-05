package org.heyner.excelutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailableFunctionsTest {
    @Test
    void testGetConfigKey() {
        assertEquals("analyzetrx", AvailableFunctions.ANALYZETRX.getConfigKey());
        assertEquals("correctionimputation", AvailableFunctions.CORRECTIONIMPUTATION.getConfigKey());
        assertEquals("directoryparser", AvailableFunctions.DIRECTORYPARSER.getConfigKey());
        assertEquals("formattrx", AvailableFunctions.FORMATTRX.getConfigKey());
        assertEquals("formatactivity", AvailableFunctions.FORMATACTIVITY.getConfigKey());
        assertEquals("fusiontrx", AvailableFunctions.FUSIONTRX.getConfigKey());
    }
    @Test
    void testIsFunctionValid_withValidKeys() {
        assertTrue(AvailableFunctions.isFunctionValid("analyzetrx"));
        assertTrue(AvailableFunctions.isFunctionValid("CORRECTIONIMPUTATION")); // test case-insensitive
        assertTrue(AvailableFunctions.isFunctionValid("FormatTrx")); // test mixed case
    }
    @Test
    void testIsFunctionValid_withInvalidKeys() {
        assertFalse(AvailableFunctions.isFunctionValid("unknownfunction"));
        assertFalse(AvailableFunctions.isFunctionValid(""));
        assertFalse(AvailableFunctions.isFunctionValid(null));
    }
}