package org.heyner.excelutils.fusiontrx;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class FusionTRX implements CommandService {
    private final FusionProcessor fusionProcessor;

    private static final String STARTING_FUSION_LOG = "Starting FusionTRX with directory: {} and output path: {}";
    private static final String FUSION_COMPLETED_LOG = "FusionTRX completed successfully.";

    @Override
    public void execute(String... args) {
        FusionTRXArgs parsed = mapArgs(args);
        execute(parsed);
    }

    public void execute(FusionTRXArgs args) {
        String directoryToProcess = (args.directory().toString().isEmpty()) ? System.getProperty("user.dir")+"\\" : args.directory().toString();
        String pathFusion = args.outputPath().toString();
        log.info(STARTING_FUSION_LOG, directoryToProcess, pathFusion);
        fusionProcessor.process(directoryToProcess,pathFusion);
        log.info(FUSION_COMPLETED_LOG);
    }

    private FusionTRXArgs mapArgs(String[] args) {
        return new FusionTRXArgs(
                java.nio.file.Path.of(args[1]),
                java.nio.file.Path.of(args[2])
        );
    }

    @Override
    public String getCommandName() {
        return "fusiontrx";
    }
}
