package org.heyner.excelutils.application.commands.fusiontrx;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class FusionTRX implements Command<FusionTRXArgs> {
    private final FusionProcessor fusionProcessor;

    private static final String STARTING_FUSION_LOG = "Starting FusionTRX with directory: {} and output path: {}";
    private static final String FUSION_COMPLETED_LOG = "FusionTRX completed successfully.";

    @Override
    public void execute(FusionTRXArgs args) {
        String directoryToProcess = (args.directory().toString().isEmpty()) ? System.getProperty("user.dir")+"\\" : args.directory().toString();
        String pathFusion = args.outputPath().toString();
        log.info(STARTING_FUSION_LOG, directoryToProcess, pathFusion);
        fusionProcessor.process(directoryToProcess,pathFusion);
        log.info(FUSION_COMPLETED_LOG);
    }

    @Override
    public String name() {
        return "fusiontrx";
    }

    @Override
    public FusionTRXArgs parse(String[] args) {
        return new FusionTRXArgs(Path.of(args[1]), Path.of(args[2]));
    }
}
