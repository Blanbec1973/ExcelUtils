package org.heyner.excelutils.utils.filenaming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.utils.PrefixReader;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResultNamer {
    private final PrefixReader excelPrefixReader;
    private final FsRenamePort fsRenamer;

    public void renameIfNeeded(Path inputName, String sheet, String cell) {
        if (hasFileNumericPrefix(inputName.getFileName().toString())) return;

        String prefix = excelPrefixReader.read(inputName.toString(), sheet, cell);

        String baseName = inputName.getFileName().toString();
        Path parent = inputName.getParent();
        Path target = (parent != null)
                    ? parent.resolve(prefix + "-" + baseName)
                    : Path.of(prefix + "-" + baseName);

        fsRenamer.rename(inputName.toString(), target.toString());
    }

    public boolean hasFileNumericPrefix(String fileName) {
        if (fileName.length()<15) return false;
        String prefix = fileName.substring(0,15);
        return (prefix.matches("\\d+")) ;
    }
}
