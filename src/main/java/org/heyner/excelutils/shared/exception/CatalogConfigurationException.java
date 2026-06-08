package org.heyner.excelutils.shared.exception;

import lombok.Getter;

@Getter
public class CatalogConfigurationException extends FunctionalException {

    private final String detail;

    public CatalogConfigurationException(String detail, int exitCode) {
        super("Catalog configuration error", exitCode);
        this.detail = detail;
    }
}