package org.heyner.excelutils;

import java.io.IOException;

public interface CommandService {
    String getCommandName();
    void execute(String... args) throws IOException;
}
