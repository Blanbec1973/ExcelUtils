package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.commands.CommandArgs;
import org.heyner.excelutils.commands.CommandService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@SuppressWarnings("squid:S2187")
public class ServiceTest implements CommandService {

    @Override
    public String getCommandName() {
        return "servicetest";
    }

    @Override
    public void execute(CommandArgs args) throws IOException {
        log.info("TestFormatActivityService executed with args: " + args);
    }
}
