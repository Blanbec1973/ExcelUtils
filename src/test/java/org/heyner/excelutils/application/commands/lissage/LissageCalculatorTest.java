package org.heyner.excelutils.application.commands.lissage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LissageCalculatorTest {

    private final LissageCalculator calculator = new LissageCalculator();

    @Test
    void revenueForTargetMargin_returns_expected_revenue() {
        double revenue = calculator.revenueForTargetMargin(100.0d, 0.20d);
        assertEquals(125.0d, revenue, 1.0e-9);
    }

    @Test
    void revenueForTargetMargin_throws_when_margin_is_one() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.revenueForTargetMargin(100.0d, 1.0d));
    }
}

