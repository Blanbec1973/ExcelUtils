package org.heyner.excelutils;

public enum AvailableFunctions {
    ANALYZETRX("analyzetrx"),
    CORRECTIONIMPUTATION("correctionimputation"),
    DIRECTORYPARSER("directoryparser"),
    FORMATTRX("formattrx"),
    FORMATACTIVITY("formatactivity"),
    FUSIONTRX("fusiontrx");

    private final String configKey;

    AvailableFunctions(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }

    public static boolean isFunctionValid(String key) {
        for (AvailableFunctions func : values()) {
            if (func.configKey.equalsIgnoreCase(key)) {
            return true;
            }
        }
        return false;
    }

}
