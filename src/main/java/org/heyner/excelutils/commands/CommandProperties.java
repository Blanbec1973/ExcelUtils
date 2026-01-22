package org.heyner.excelutils.commands;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "command")
@Slf4j
public class CommandProperties {
    private Map<String, CommandConfig> commands = new HashMap<>();
    public Map<String, CommandConfig> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, CommandConfig> commands) {
        log.debug("Setter called with configs: {}", commands);
        this.commands = commands;
    }

    @PostConstruct
    public void init() {
        log.debug("Command configs loaded : " + commands.keySet());
    }

    public static class CommandConfig {
        private int counterarguments;
        public int getCounterarguments() {
            return counterarguments;
        }

        public void setCounterarguments(int counterarguments) {
            this.counterarguments = counterarguments;
        }
    }
}
