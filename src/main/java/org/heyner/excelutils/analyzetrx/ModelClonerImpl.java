package org.heyner.excelutils.analyzetrx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.ExitCodes;
import org.heyner.excelutils.exceptions.CloneModelException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class ModelClonerImpl implements ModelCloner {

    private static final String CURRENT_PATH_LOG = "CurrentPath: {}";
    private static final String ABSOLUTE_PATH_IN_LOG = "Absolute path in: {}";
    private static final String ABSOLUTE_PATH_OUT_LOG = "Absolute path out: {}";

    @Override
    public void copy(Path in, Path out) {
        log.info("Copy {} to {}.", in, out);
        log.debug(CURRENT_PATH_LOG, System.getProperty("user.dir"));
        log.debug(ABSOLUTE_PATH_IN_LOG, in.toAbsolutePath());
        log.debug(ABSOLUTE_PATH_OUT_LOG, out.toAbsolutePath());
        try {
            Files.copy(in, out);
        } catch (IOException e) {
            log.error("Model copy error", e);
            throw new CloneModelException(e.getMessage(), ExitCodes.FILE_PROCESSING_ERROR);

        }
    }
}
