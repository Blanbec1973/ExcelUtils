package org.heyner.excelutils.utils.filenaming;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class FsRenamer implements FsRenamePort {

    public void rename(String inputName, String outputName) {
        File inputFile = new File(inputName);
        File outputFile = new File(outputName);

        if (inputFile.renameTo(outputFile)) log.debug("New name : {}", outputFile);
    }
}
