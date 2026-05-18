package org.heyner.excelutils.application.commands.lissage;

import org.springframework.stereotype.Component;

@Component
public class LissageCalculator {

    private static final double EPSILON = 1.0e-9;

    public double revenueForTargetMargin(double cost, double targetMargin) {
        if (Math.abs(1.0d - targetMargin) < EPSILON) {
            throw new IllegalArgumentException("targetMargin cannot be 1.0");
        }
        return cost / (1.0d - targetMargin);
    }
}

