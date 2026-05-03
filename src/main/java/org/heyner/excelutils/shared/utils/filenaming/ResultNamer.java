package org.heyner.excelutils.shared.utils.filenaming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.infrastructure.filesystem.FsRenamePort;
import org.heyner.excelutils.shared.utils.PrefixReader;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResultNamer {
    private final PrefixReader excelPrefixReader;
    private final FsRenamePort fsRenamer;
    private static final int PREFIX_LENGTH = 15;
    private static final Pattern NUMERIC_PREFIX_PATTERN = Pattern.compile("^\\d{15}");

    public void renameIfNeeded(Path inputName, String sheet, String cell) {
        log.debug("Checking whether file needs prefix-based rename: {}", inputName);
        if (hasFileNumericPrefix(inputName.getFileName().toString())) {
            log.debug("File already has numeric prefix: {}", inputName);
            return;
        }

        String prefix = excelPrefixReader.read(inputName.toString(), sheet, cell);
        log.info("Read prefix {} from {}:{}{}", prefix, inputName, sheet, cell);

        String baseName = inputName.getFileName().toString();
        Path parent = inputName.getParent();
        Path target = (parent != null)
                    ? parent.resolve(prefix + "-" + baseName)
                    : Path.of(prefix + "-" + baseName);

        fsRenamer.rename(inputName.toString(), target.toString());
        log.info("Renaming {} to {}", inputName, target);
    }

    public boolean hasFileNumericPrefix(String fileName) {
        return fileName.length() >= PREFIX_LENGTH &&
                NUMERIC_PREFIX_PATTERN.matcher(fileName).find();
    }
}
