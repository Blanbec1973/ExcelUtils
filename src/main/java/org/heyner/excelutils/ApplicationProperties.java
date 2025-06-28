package org.heyner.excelutils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String version;
    private String projectName;

    public void setVersion(String version) {
        this.version = version;
    }
    public String getVersion() {
        return version;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }



}
