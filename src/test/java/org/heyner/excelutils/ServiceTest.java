package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.CommandArgs;
import org.heyner.excelutils.application.commands.core.CommandService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@SuppressWarnings("squid:S2187")
public class ServiceTest implements CommandService<CommandArgs> {

    @Override
    public String getCommandName() {
        return "servicetest";
    }

    @Override
    public void execute(CommandArgs args) throws IOException {
        log.info("Test Service executed with args: " + args);
    }
}
