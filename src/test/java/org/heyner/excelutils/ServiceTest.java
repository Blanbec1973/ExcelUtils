package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@SuppressWarnings("squid:S2187")
public class ServiceTest implements CommandService {

    @Override
    public String getCommandName() {
        return "servicetest";
    }

    @Override
    public void execute(String[] args) {
        log.info("TestFormatActivityService executed with args: " + String.join(", ", args));
    }
}
