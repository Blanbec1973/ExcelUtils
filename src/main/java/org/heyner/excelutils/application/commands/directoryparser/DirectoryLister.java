
package org.heyner.excelutils.application.commands.directoryparser;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.shared.constants.ExitCodes;
import org.heyner.excelutils.shared.exceptions.FatalApplicationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Component
@Slf4j
public class DirectoryLister {
    public List<Path> listXlsx(Path directory) {
        log.debug("Listing xlsx files in {}", directory);
        try (var s = Files.list(directory)) {
            List<Path> result = s.filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".xlsx") && Files.isRegularFile(p);
                    })
                    .toList();

            log.debug("Found {} xlsx file(s) in {}", result.size(), directory);

            return result;
        } catch (IOException e) {
            throw new FatalApplicationException(
                    "Error processing file: " + directory,
                    e,
                    ExitCodes.FILE_PROCESSING_ERROR
            );
        }
    }
}
