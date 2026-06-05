package org.heyner.excelutils.utils;

import lombok.extern.slf4j.Slf4j;
import org.heyner.excelutils.shared.utils.DateTemplateExpanderImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
        assertTrue(yearFile.matches("\\d+"));
        int yearFileInt = Integer.parseInt(yearFile);
        assertTrue(yearFileInt>=2025);
    }

    @Test
    void generateFileNameWithoutDate() {
        String result = dateTemplateExpander.expand("File-name.txt");
        assertEquals("File-name.txt",result);
    }

    @Test
    void expandedDateMatchesTodayFormat() {
        String today = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String result = dateTemplateExpander.expand("report-aaaa-mm-jj.xlsx");
        assertEquals("report-" + today + ".xlsx", result);
    }

    @Test
    void expandReplacesAllOccurrencesOfTemplate() {
        String today = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String result = dateTemplateExpander.expand("aaaa-mm-jj_backup_aaaa-mm-jj.csv");
        assertEquals(today + "_backup_" + today + ".csv", result);
    }

    @Test
    void expandWithEmptyStringReturnsEmptyString() {
        String result = dateTemplateExpander.expand("");
        assertEquals("", result);
    }

    @Test
    void expandWithOnlyTemplatePlaceholderReturnsDate() {
        String today = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String result = dateTemplateExpander.expand("aaaa-mm-jj");
        assertEquals(today, result);
    }

    @Test
    void expandPreservesTextAroundPlaceholder() {
        String today = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String result = dateTemplateExpander.expand("PREFIX_aaaa-mm-jj_SUFFIX");
        assertEquals("PREFIX_" + today + "_SUFFIX", result);
    }

    @Test
    void expandedDateContainsValidYearMonthDay() {
        String result = dateTemplateExpander.expand("aaaa-mm-jj");
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }
}