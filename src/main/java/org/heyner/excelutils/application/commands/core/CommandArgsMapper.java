package org.heyner.excelutils.application.commands.core;

public interface CommandArgsMapper {
    String supports(); // nom de la commande
    CommandArgs map(String[] args);
}