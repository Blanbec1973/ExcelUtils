package org.heyner.excelutils.utils.filenaming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.utils.PrefixReader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResultNamer {
    private final PrefixReader excelPrefixReader;
    private final FsRenamePort fsRenamer;

    public void renameIfNeeded(String inputName, String sheet, String cell) {
        if (hasFileNumericPrefix(inputName)) return;

        String prefix = excelPrefixReader.read(inputName, sheet, cell);
        fsRenamer.rename(inputName, prefix + "-" + inputName);
    }

    public boolean hasFileNumericPrefix(String fileName) {
        if (fileName.length()<15) return false;
        String prefix = fileName.substring(0,15);
        return (prefix.matches("\\d+")) ;
    }
}
