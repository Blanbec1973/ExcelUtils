package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@SuppressWarnings("squid:S2187")
public class CommandTest implements Command<CommandTestArgs> {

    @Override
    public String name() {
        return "servicetest";
    }

    @Override
    public CommandTestArgs parse(String[] args) {
        return new CommandTestArgs("servicetest");
    }

    @Override
    public void execute(CommandTestArgs args) throws Exception {
        log.info("Test Service executed with args: " + args);
    }

}

record CommandTestArgs(String value) implements CommandArgs {}
