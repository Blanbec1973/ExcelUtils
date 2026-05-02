package org.heyner.excelutils.shared.utils;

import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateTemplateExpanderImpl implements DateTemplateExpander {
    public String expand(String template) {
        LocalDate today = LocalDate.now();
        String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return template.replace(ExcelConstants.DATE_TEMPLATE, date);
    }

}
