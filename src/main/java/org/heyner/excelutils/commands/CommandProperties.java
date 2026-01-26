package org.heyner.excelutils.commands;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties
@Slf4j
public class CommandProperties {

    private Map<String, Integer> counterarguments = new HashMap<>();

    public Map<String, Integer> getCounterarguments() {
        return counterarguments;
    }

    public void setCounterarguments(Map<String, Integer> counterarguments) {
        this.counterarguments = counterarguments;
    }

    @PostConstruct
    public void init() {
        log.debug("Config loaded : " + counterarguments);
    }
}

