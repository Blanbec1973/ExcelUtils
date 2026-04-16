package org.heyner.excelutils.exitcode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CustomExitCodeGenerator;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FunctionalException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultExitCodeHandler implements ExitCodeHandler {

    private final CustomExitCodeGenerator exitCodeGenerator;

    @Override
    public void handle(Throwable t) {
        switch (t) {
            case GracefulExitException e -> {
                log.info("Program ends normally : {}", e.getMessage());
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case FunctionalException e -> {
                log.error(e.getMessage());
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case FatalApplicationException e -> {
                log.error("Fatal Error", e);
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            default -> {
                log.error("Unexpected error", t);
                exitCodeGenerator.setExitCode(1);
            }
        }
    }
}
