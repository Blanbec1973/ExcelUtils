package org.heyner.excelutils.application.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ConfigurationProperties(prefix = "formatactivity")
@Slf4j
public class FormatActivityConfig {
    @Setter
    private List<Integer> hiddencolumns;

    private Set<Integer> hideColumnsSet;

    @PostConstruct
    public void init() {
        if (hiddencolumns == null || hiddencolumns.isEmpty()) {
            throw new IllegalStateException("Missing configuration property formatactivity.hiddencolumns");
        }
        this.hideColumnsSet = new HashSet<>(hiddencolumns);
        log.debug("FormatActivity Config loaded : {}", hideColumnsSet);
    }
}
