package org.heyner.excelutils.application.commands.directoryparser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.heyner.excelutils.application.commands.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.cli.CliPrinter;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FileProcessorException;
import org.heyner.excelutils.shared.exceptions.GracefulExitException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryParser implements Command<DirectoryParserArgs> {
    private final List<FileProcessor> processors;
    private final DirectoryLister lister;
    private final FileClassifier classifier;
    private final CliPrinter cliPrinter;

    private static final String BEGIN_FUNCTION_LOG = "Beginning function : {}";
    private static final String PROCESSING_LOG = "Processing : {}";
    private static final String PROCESS_FILE_LOG = "Processing file {} as {}";

    @Override
    public String name() {
        return "directoryparser";
    }

    @Override
    public DirectoryParserArgs parse(String[] args) {
        validate(args);
        return new DirectoryParserArgs(Path.of(args[1]));
    }

    private void validate(String[] args) {
        if (args[1] == null || args[1].isBlank()) {
            throw new IllegalArgumentException("ERROR: directory is required: ");
        }

        Path directory = Path.of(args[1]);
        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("ERROR: directory not found: " + directory);
        }
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("ERROR: expected a directory: " + directory);
        }
    }

    @Override
    public void execute(DirectoryParserArgs args) {
        String directoryToProcess = args
                .directory().toString();
        log.debug(BEGIN_FUNCTION_LOG,
                this.getClass().getSimpleName());
        log.info(PROCESSING_LOG,directoryToProcess);
        List<Path> paths = lister.listXlsx(args.directory());

        if (paths.isEmpty()) {
            throw new GracefulExitException(
                    "No file found to process in " + directoryToProcess,
                    ExitCodes.SUCCESS
            );
        }
        processFiles(paths);
        cliPrinter.info("SUCCESS: processing directory completed");
    }

    public void processFiles(List<Path> paths) {
        for (Path p : paths) {
            applyProcessors(p); //Order from Spring
        }
    }

    private void applyProcessors(Path filePath) {
        FileType type = classifier.classify(filePath);
        log.info(PROCESS_FILE_LOG, filePath.getFileName(), type);

        processors.stream()
                .filter(p-> p.getSupportedFileType()==type)
                .forEach(p-> {
                    try {
                        p.process(filePath);
                    } catch (IOException e) {
                        throw new FileProcessorException(
                                p.getClass().getSimpleName(),
                                e,
                                ExitCodes.FILE_PROCESSING_ERROR
                        );
                    }
                });
    }
}
