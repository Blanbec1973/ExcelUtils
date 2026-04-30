package org.heyner.excelutils.directoryparser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.ExitCodes;
import org.heyner.excelutils.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.exceptions.FileProcessorException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryParser implements CommandService {
    private final List<FileProcessor> processors;
    private final DirectoryLister lister;
    private Path[] listPaths;

    private static final String BEGIN_FUNCTION_LOG = "Beginning function: {}";
    private static final String PROCESSING_LOG = "Processing {}";
    private static final String PROCESS_FILE_LOG = "ProcessList file: {}";

    @Override
    public String getCommandName() {
        return "directoryparser";
    }

    @Override
    public void execute(String... args) throws IOException {
        DirectoryParserArgs parsed = mapArgs(args);
        execute(parsed);
    }

    public void execute(DirectoryParserArgs args) throws IOException {
        String directoryToProcess = args.directory().toString();
        log.debug(BEGIN_FUNCTION_LOG,
                this.getClass().getSimpleName());
        log.info(PROCESSING_LOG,directoryToProcess);
        listPaths = lister.listXlsx(directoryToProcess).toArray(new Path[0]);

        if (isListFilesEmpty()) {
            throw new GracefulExitException("No file to process in " + directoryToProcess, 0);
        }
        processList();
    }

    private DirectoryParserArgs mapArgs(String[] args) {
        return new DirectoryParserArgs(
                Path.of(args[1])
        );
    }

    public boolean isListFilesEmpty() {
        return listPaths == null || listPaths.length == 0;
    }

    public void processList() {
        for (Path p : listPaths) {
            log.info(PROCESS_FILE_LOG, p.getFileName());
            processWithProcessors(p);
        }
    }

    private void processWithProcessors(Path filePath) {

        for (FileProcessor processor : processors) {
            if (processor.supports(filePath)) {
                try {
                    processor.process(filePath);
                } catch (IOException e) {
                    throw new FileProcessorException(
                            processor.getClass().getSimpleName(),
                            e,
                            ExitCodes.FILE_PROCESSING_ERROR
                    );
                }
            }
        }
    }
}
