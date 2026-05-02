package org.heyner.excelutils.application.commands.directoryparser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.CommandService;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.application.commands.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.shared.exceptions.FileProcessorException;
import org.heyner.excelutils.shared.exceptions.GracefulExitException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryParser implements CommandService<DirectoryParserArgs> {
    private final List<FileProcessor> processors;
    private final DirectoryLister lister;
    private final FileClassifier classifier;

    private static final String BEGIN_FUNCTION_LOG = "Beginning function: {}";
    private static final String PROCESSING_LOG = "Processing {}";
    private static final String PROCESS_FILE_LOG = "ProcessList file: {}";

    @Override
    public String getCommandName() {
        return "directoryparser";
    }

    @Override
    public void execute(DirectoryParserArgs args) throws IOException {
        String directoryToProcess = args
                .directory().toString();
        log.debug(BEGIN_FUNCTION_LOG,
                this.getClass().getSimpleName());
        log.info(PROCESSING_LOG,directoryToProcess);
        List<Path> paths = lister.listXlsx(args.directory());

        if (paths.isEmpty()) {
            throw new GracefulExitException("No file to process in " + directoryToProcess, 0);
        }
        processList(paths);
    }

    public void processList(List<Path> paths) {
        for (Path p : paths) {
            log.info(PROCESS_FILE_LOG, p.getFileName());
            processWithProcessors(p);
        }
    }

    private void processWithProcessors(Path filePath) {
        FileType type = classifier.classify(filePath);

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
