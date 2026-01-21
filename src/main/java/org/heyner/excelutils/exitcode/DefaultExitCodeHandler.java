package org.heyner.excelutils.exitcode;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CustomExitCodeGenerator;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FunctionalException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultExitCodeHandler implements ExitCodeHandler {

    private final CustomExitCodeGenerator exitCodeGenerator;

    public DefaultExitCodeHandler(CustomExitCodeGenerator exitCodeGenerator) {
        this.exitCodeGenerator = exitCodeGenerator;
    }

    @Override
    public void handle(Throwable t) {
        if (t instanceof GracefulExitException e) {
            log.info("Program ends normally : {}", e.getMessage());
            exitCodeGenerator.setExitCode(e.getExitCode());
        } else if (t instanceof FunctionalException e) {
            log.error(e.getMessage());
            exitCodeGenerator.setExitCode(e.getExitCode());
        } else if (t instanceof FatalApplicationException e) {
            log.error("Fatal Error", e);
            exitCodeGenerator.setExitCode(e.getExitCode());
        } else {
            log.error("Unexpected error", t);
            exitCodeGenerator.setExitCode(1);
        }
    }
}

