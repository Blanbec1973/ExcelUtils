package org.heyner.excelutils.commands;


import java.util.Optional;
import java.util.Set;

public interface CommandSpecCatalog {
    Optional<CommandSpec> find(String commandName);
    Set<String> names(); // pratique pour help() / debug
}

