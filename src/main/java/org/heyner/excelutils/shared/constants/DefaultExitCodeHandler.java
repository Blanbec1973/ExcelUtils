package org.heyner.excelutils.shared.constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.cli.CommandHelpCatalog;
import org.heyner.excelutils.cli.CommandHelpEntry;
import org.heyner.excelutils.shared.exceptions.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultExitCodeHandler implements ExitCodeHandler {

    private final CustomExitCodeGenerator exitCodeGenerator;
    private final CommandHelpCatalog helpCatalog;
    private final CliPrinter cliPrinter;

    private static final String PROGRAM_ENDS_NORMALLY_LOG = "SUCCESS - %s";
    private static final String UNEXPECTED_ERROR_LOG = "ERROR: Unexpected error";

    @Override
    public void handle(Throwable t) {
        switch (t) {
            case GracefulExitException e -> {
                cliPrinter.info(PROGRAM_ENDS_NORMALLY_LOG.formatted(e.getMessage()));
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case CatalogConfigurationException e -> {
                log.error("""
            ERROR: invalid command catalog configuration
            Detail: {}
            """,
                        e.getDetail());

                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case InvalidArgumentCountException e -> {
                String usage = helpCatalog.find(e.getCommandName())
                        .map(CommandHelpEntry::usage)
                        .orElse("excelutils help");

                log.error("""
                ERROR: invalid number of arguments
                Command: {}
                Expected: {}
                Received: {}
                """, e.getCommandName(), e.getExpected(), e.getActual());
                    cliPrinter.error("""
            ERROR: invalid number of arguments
            Command: %s
            Expected: %s
            Received: %s
            Usage: %s
            """.formatted(e.getCommandName(),
                        e.getExpected(),
                        e.getActual(),
                        usage));

                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case InvalidFunctionException e -> {
                log.error("""
                ERROR: unknown command: {}
                Usage: excelutils help""",e.getFunctionName());
                cliPrinter.info("""
                ERROR: unknown command: %s
                Usage: excelutils help""".formatted(e.getFunctionName()));
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case MissingConfigurationException e -> {
                cliPrinter.error("""
            ERROR: missing command
            Usage: excelutils help
            """);
                log.error("ERROR: missing command");
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case FunctionalException e -> {
                log.error(e.getMessage());
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            case FatalApplicationException e -> {
                cliPrinter.error("""
                        ERROR: Fatal error while processing %s
                        """.formatted(e.getRessource()));
                exitCodeGenerator.setExitCode(e.getExitCode());
            }
            default -> {
                cliPrinter.error("ERROR: unexpected error");
                log.error("{} - {}", UNEXPECTED_ERROR_LOG, t.getClass().getName(), t);
                exitCodeGenerator.setExitCode(ExitCodes.UNEXPECTED_ERROR);
            }
        }
    }
}
