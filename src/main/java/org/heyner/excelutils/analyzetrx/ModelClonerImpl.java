package org.heyner.excelutils.analyzetrx;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.exceptions.CloneModelException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class ModelClonerImpl implements ModelCloner {
    @Override
    public void copy(Path in, Path out) {
        log.info("Copy {} to {}.", in, out);
        log.debug("CurrentPath : " + System.getProperty("user.dir"));
        log.debug("Absolute path in : " + in.toAbsolutePath());
        log.debug("Absolute path out : " + out.toAbsolutePath());
        try {
            Files.copy(in, out);
        } catch (IOException e) {
            log.error("Model copy error", e);
            throw new CloneModelException(e.getMessage(), -1);

        }
    }
}
