package org.heyner.excelutils.commands;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties
@Slf4j
public class CommandProperties {

    private Map<String, Integer> counterarguments = new HashMap<>();

    @PostConstruct
    public void init() {
        log.debug("Config loaded : " + counterarguments);
    }
}

