package org.heyner.excelutils.application.commands.fusiontrx;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.application.commands.core.Command;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;

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
        validate(args);
        return new FusionTRXArgs(Path.of(args[1]), Path.of(args[2]));
    }

    private void validate(String[] args) {
        if (args[1] == null || args[1].isBlank()) {
            throw new IllegalArgumentException("Input directory path must not be blank");
        }

        Path inputDir = Path.of(args[1]);
        if (!Files.exists(inputDir)) {
            throw new IllegalArgumentException("Input directory does not exist: " + inputDir);
        }
        if (!Files.isDirectory(inputDir)) {
            throw new IllegalArgumentException("Input path is not a directory: " + inputDir);
        }

        if (args[2] == null || args[2].isBlank()) {
            throw new IllegalArgumentException("Output directory path must not be blank");
        }

        Path outputDir = Path.of(args[2]);
        if (!Files.exists(outputDir)) {
            throw new IllegalArgumentException("Output directory does not exist: " + outputDir);
        }
        if (!Files.isDirectory(outputDir)) {
            throw new IllegalArgumentException("Output path is not a directory: " + outputDir);
        }
    }
}
