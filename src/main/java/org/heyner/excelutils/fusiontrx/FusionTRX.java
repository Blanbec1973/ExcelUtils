package org.heyner.excelutils.fusiontrx;


import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class FusionTRX implements CommandService {
    @Autowired
    private final FusionProcessor fusionProcessor;

    public FusionTRX(FusionProcessor fusionProcessor) {
        this.fusionProcessor = fusionProcessor;
    }

    public void execute(String... args) {
        String directoryToProcess = (args[1].equals("")) ? System.getProperty("user.dir")+"\\" : args[1];
        String pathFusion = args[2];
        log.info("Starting FusionTRX with directory: {} and output path: {}", directoryToProcess, pathFusion);
        fusionProcessor.process(directoryToProcess,pathFusion);
        log.info("FusionTRX completed successfully.");
    }
    @Override
    public String getCommandName() {
        return "fusiontrx";
    }
}
