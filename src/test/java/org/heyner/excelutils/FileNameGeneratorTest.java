package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class FileNameGeneratorTest {

    @Test
    void generateFileNameWithDate() {
        String result = FileNameGenerator.generateFileNameWithDate("File-name-aaaa-mm-jj.txt");
        String yearFile = result.substring(10,14);
        log.info("yearFile : {}", yearFile);
        assertTrue(yearFile != null && yearFile.matches("\\d+"));
        int yearFileInt = Integer.parseInt(yearFile);
        assertTrue(yearFileInt>=2025);
    }

    @Test
    void generateFileNameWithoutDate() {
        String result = FileNameGenerator.generateFileNameWithDate("File-name.txt");
        assertEquals("File-name.txt",result);
    }

    @Test
    void checkAbsenceOfPrefixTest() {
        assertTrue(FileNameGenerator.hasFileNoPrefix(new File("toto.com")));
        assertFalse(FileNameGenerator.hasFileNoPrefix(new File("300000000073657-toto.com")));
    }


}