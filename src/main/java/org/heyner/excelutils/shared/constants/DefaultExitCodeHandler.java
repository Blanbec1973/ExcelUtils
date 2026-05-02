package org.heyner.excelutils.shared.constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.heyner.excelutils.shared.exceptions.FunctionalException;
import org.heyner.excelutils.shared.exceptions.GracefulExitException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultExitCodeHandler implements ExitCodeHandler {

    private final CustomExitCodeGenerator exitCodeGenerator;

    private static final String PROGRAM_ENDS_NORMALLY_LOG = "Program ends normally: {}";
    private static final String FATAL_ERROR_LOG = "Fatal Error";
    private static final String UNEXPECTED_ERROR_LOG = "Unexpected error";

    @Override
    public void handle(Throwable t) {
        switch (t) {
            case GracefulExitException e -> {
                log.info(PROGRAM_ENDS_NORMALLY_LOG, e.getMessage());
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case FunctionalException e -> {
                log.error(e.getMessage());
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case FatalApplicationException e -> {
                log.error(FATAL_ERROR_LOG, e);
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            default -> {
                log.error(UNEXPECTED_ERROR_LOG, t);
                exitCodeGenerator.setExitCode(ExitCodes.UNEXPECTED_ERROR);
            }
        }
    }
}
