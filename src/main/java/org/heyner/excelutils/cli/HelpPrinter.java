package org.heyner.excelutils.cli;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.shared.config.ApplicationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpPrinter {

    private final ApplicationProperties applicationProperties;
    private final Slf4jCliPrinter cliPrinter;
    private final CommandHelpCatalog catalog;

    public void printAll() {
        cliPrinter.info("ExcelUtils "+applicationProperties.getVersion());
        cliPrinter.blankLine();
        for (CommandHelpEntry entry : catalog.all()) {
            printEntry(entry);
        }
    }

    public void printCommand(String name) {
        catalog.find(name).ifPresentOrElse(
                this::printEntry,
                () -> cliPrinter.error("Unknown command: " + name)
        );
    }

    private void printEntry(CommandHelpEntry entry) {
        cliPrinter.info(entry.name());
        cliPrinter.info("  " + entry.description());
        cliPrinter.info("  Usage: " + entry.usage());
        cliPrinter.info("  Example: " + entry.example());
        cliPrinter.blankLine();
    }
}