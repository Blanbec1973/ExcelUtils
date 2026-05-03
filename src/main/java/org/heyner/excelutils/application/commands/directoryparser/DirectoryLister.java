
package org.heyner.excelutils.application.commands.directoryparser;

import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Component
public class DirectoryLister {
    public List<Path> listXlsx(Path directory) {
        try (var s = Files.list(directory)) {
            return s.filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".xlsx") && Files.isRegularFile(p);
                    })
                    .toList();
        } catch (IOException e) {
            throw new FatalApplicationException("Error processing file: " + directory,
                    e, ExitCodes.FILE_PROCESSING_ERROR);
        }
    }
}
