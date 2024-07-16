package commun;

import fusiontrx.FusionTRX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parameter.Parameter;

import java.io.IOException;

public class ExcelUtils {
    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
    private final Parameter param = new Parameter("config.properties");

    public static void main(String[] args) throws IOException {
        new ExcelUtils(args);
    }

    public ExcelUtils(String[] args) throws IOException {
        new ArgsChecker(args,param);

        String projectName = param.getProperty("projectName");
        String version = param.getProperty("version");
        logger.info("Beginning : {} version {} function {}",
                projectName,
                version,
                args[0]);

        AvailableFunctions function = AvailableFunctions.valueOf(args[0]);

        switch(function) {
            case FUSION_TRX:
                new FusionTRX(args);
        }
    }
}
