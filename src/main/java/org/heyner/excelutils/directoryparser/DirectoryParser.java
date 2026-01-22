package org.heyner.excelutils.directoryparser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.heyner.excelutils.directoryparser.processors.FileProcessor;
import org.heyner.excelutils.exceptions.FileProcessorException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectoryParser implements CommandService {
    private final List<FileProcessor> processors;
    private final DirectoryLister lister;
    private File[] listFiles;

    @Override
    public String getCommandName() {
        return "directoryparser";
    }

    @Override
    public void execute(String... args) throws IOException {
        String directoryToProcess = args[1];
        log.debug("Beginning function : {}",
                this.getClass().getSimpleName());
        log.info("Processing {}",directoryToProcess);
        listFiles = lister.listXlsx(directoryToProcess);

        if (isListFilesEmpty()) {
            throw new GracefulExitException("No file to process in " + directoryToProcess, 0);
        }
        processList();
    }

    public boolean isListFilesEmpty() {
        return listFiles == null || listFiles.length == 0;
    }

    public void processList() {
        for (File f : listFiles) {
            log.info("ProcessList file : {}", f.getName());
            processWithProcessors(f);
        }
    }

    private void processWithProcessors(File file) {

        for (FileProcessor processor : processors) {
            if (processor.supports(file)) {
                try {
                    processor.process(file);
                } catch (IOException e) {
                    throw new FileProcessorException(
                            processor.getClass().getSimpleName(),
                            e,
                            -1
                    );
                }
            }
        }
    }
}
