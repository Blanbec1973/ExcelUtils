package org.heyner.excelutils.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "analyzetrx")
public class AnalyzeTRXConfig {
    // Getters et setters
    private String pathModel;
    private String pathResultFile;
    private String sheetIn;
    private String sheetOut;

}

