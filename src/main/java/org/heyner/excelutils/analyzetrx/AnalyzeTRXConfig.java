package org.heyner.excelutils.analyzetrx;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "analyzetrx")
public class AnalyzeTRXConfig {
    private String pathModel;
    private String pathResultFile;
    private String sheetIn;
    private String sheetOut;

    // Getters et setters
    public String getPathModel() { return pathModel; }
    public void setPathModel(String pathModel) { this.pathModel = pathModel; }

    public String getPathResultFile() { return pathResultFile; }
    public void setPathResultFile(String pathResultFile) { this.pathResultFile = pathResultFile; }

    public String getSheetIn() { return sheetIn; }
    public void setSheetIn(String sheetIn) { this.sheetIn = sheetIn; }

    public String getSheetOut() { return sheetOut; }
    public void setSheetOut(String sheetOut) { this.sheetOut = sheetOut; }
}

