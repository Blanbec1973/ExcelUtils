package org.heyner.excelutils.application.commands.core;


import java.util.Optional;
import java.util.Set;

public interface CommandSpecCatalog {
    Optional<CommandSpec> find(String commandName);
    Set<String> names(); // pratique pour help() / debug
}

