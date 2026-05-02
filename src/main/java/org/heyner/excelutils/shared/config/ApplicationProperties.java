package org.heyner.excelutils.shared.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String version;
    private String projectName;


}
