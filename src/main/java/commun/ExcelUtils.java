package commun;

import analyzetrx.AnalyzeTRX;
import directoryparser.DirectoryParser;
import format_trx.FormatTRX;
import formatactivity.FormatActivity;
import fusiontrx.FusionTRX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import parameter.Parameter;

import java.io.IOException;

public class ExcelUtils {
    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);

    public static void main(String[] args) throws IOException {
        new ExcelUtils(args);
    }

    public ExcelUtils(String[] args) throws IOException {
        ZipSecureFile.setMinInflateRatio(0.001);
        Parameter param = new Parameter("config.properties");
        new ArgsChecker(args, param);

        String projectName = param.getProperty("projectName");
        String version = param.getProperty("version");
        logger.info("Beginning : {} version {} function {}",
                projectName,
                version,
                args[0]);

        AvailableFunctions function = AvailableFunctions.valueOf(args[0]);

        switch (function) {
            case FUSION_TRX -> new FusionTRX(args);
            case ANALYZE_TRX -> new AnalyzeTRX(args);
            case FORMAT_TRX -> FormatTRX.applyFormatTRX(args);
            case FORMAT_ACTIVITY -> new FormatActivity(new String[]{args[1]});
            case DIRECTORY_PARSER -> new DirectoryParser(args[1]);
            default -> logger.error("Function not encoded {}",function);
        }
    }
}
