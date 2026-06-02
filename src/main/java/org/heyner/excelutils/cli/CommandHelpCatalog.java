package org.heyner.excelutils.cli;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommandHelpCatalog {
    public List<CommandHelpEntry> all() {
        return List.of(
                new CommandHelpEntry("analyzetrx",
                        "Analyze a TRX file and generate the result workbook",
                        "analyzetrx <trxFile>",
                        "excelutils analyzetrx \"C:\\data\\UC_PCB_PROJ_TRX.xlsx\""),

                new CommandHelpEntry("fusiontrx",
                        "Merge TRX files from a directory",
                        "fusiontrx <inputDir> <outputDir>",
                        "excelutils fusiontrx \"C:\\in\" \"C:\\out\""),

                new CommandHelpEntry("lissage",
                        "Apply smoothing on a PSR workbook",
                        "lissage <file> <row> <targetMargin>",
                        "excelutils lissage \"C:\\data\\PSR.xlsx\" 13 0.334")
        );
    }

    public Optional<CommandHelpEntry> find(String name) {
        return all().stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
    }
}