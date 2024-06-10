package commun;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestInitializerFactory {

    public TestInitializerFactory() throws IOException {
        Path path = Paths.get("target/temp");
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File("target/temp"));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"), new File("target/temp/"));
    }

}
