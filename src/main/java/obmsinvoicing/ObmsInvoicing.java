package obmsinvoicing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObmsInvoicing {
    private static final Logger logger = LogManager.getLogger(ObmsInvoicing.class);
    private final File[] listFiles;

    public static void main(String[] args) {
        String directory = (args.length == 0) ? System.getProperty("user.dir")+"\\" : args[0];
        new ObmsInvoicing(directory);
    }
    public ObmsInvoicing(String directoryToProcess) {

        Parameter param = new Parameter("config.properties");
        String projectName = param.getProperty("projectName");
        String version = param.getProperty("version");
        logger.info("Beginning : {} version {} function {}",
                projectName,
                version,
                this.getClass().getSimpleName());

        logger.info("Processing {}",directoryToProcess);
        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".pdf");
        listFiles = myDirectory.listFiles(filter);

        if (listFiles.length == 0) {
            logger.info("No file to process in {}", directoryToProcess);
            System.exit(0);
        }
        List<ObmsInvoice> invoiceList = new ArrayList<>();
        processList(invoiceList);
    }

    private void processList(List<ObmsInvoice> invoiceList) {
        for (File file : Objects.requireNonNull(listFiles)) {
            logger.info("ProcessList file : {}", file.getName());
            invoiceList.add(new ObmsInvoice(file));
        }
    }

}
