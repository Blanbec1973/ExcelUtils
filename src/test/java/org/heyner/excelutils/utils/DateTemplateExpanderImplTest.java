package org.heyner.excelutils.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class DateTemplateExpanderImplTest {
    private final DateTemplateExpanderImpl dateTemplateExpander = new DateTemplateExpanderImpl();

    @Test
    void generateFileNameWithDate() {
        String result = dateTemplateExpander.expand("File-name-aaaa-mm-jj.txt");
        String yearFile = result.substring(10,14);
        log.info("yearFile : {}", yearFile);
        assertTrue(yearFile != null && yearFile.matches("\\d+"));
        int yearFileInt = Integer.parseInt(yearFile);
        assertTrue(yearFileInt>=2025);
    }

    @Test
    void generateFileNameWithoutDate() {
        String result = dateTemplateExpander.expand("File-name.txt");
        assertEquals("File-name.txt",result);
    }
}