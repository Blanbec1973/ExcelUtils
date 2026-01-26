
package org.heyner.excelutils.directoryparser;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Component
public class DirectoryLister {
    public List<Path> listXlsx(String directory) throws IOException {
        Path dir = Path.of(directory);
        try (var s = Files.list(dir)) {
            return s.filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".xlsx") && Files.isRegularFile(p);
                    })
                    .toList();
        }
    }
}
