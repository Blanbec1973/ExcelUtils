package org.heyner.excelutils.application.commands.formatactivity;

import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.testsupport.TestInitializerFactory;
import org.heyner.excelutils.cli.Slf4jCliPrinter;
import org.heyner.excelutils.application.config.FormatActivityConfig;
import org.heyner.excelutils.infrastructure.excel.FormatActivityAdapter;
import org.heyner.excelutils.shared.config.ApachePoiConfigurer;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = {FormatActivity.class,
                           ApachePoiConfigurer.class,
                           FormatActivityAdapter.class,
                           Slf4jCliPrinter.class})
@EnableConfigurationProperties(FormatActivityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormatActivityTest {
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx";
    @Autowired
    private FormatActivity formatActivity;

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }
    @Test
    void execute() {
        formatActivity.execute(FormatActivityArgs.builder().inputFile(Path.of(fileName)).build());

        try (ExcelFile fichierExcel = ExcelFile.open(fileName)) {
            assertEquals("From Date",fichierExcel.getCellValue(ExcelConstants.DEFAULT_SHEET,0,0));
            assertEquals(15, fichierExcel.rowCount(ExcelConstants.DEFAULT_SHEET,0));
            assertEquals("Mt HT", fichierExcel.getCellValue(ExcelConstants.DEFAULT_SHEET,"AB1"));
        } catch (IOException e) {
            fail("Error reading the formatted file: " + e.getMessage());
        }
    }

    @Test
    void getCommandName() {
        assertEquals("formatactivity",formatActivity.name());
    }
}