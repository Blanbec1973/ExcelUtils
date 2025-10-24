package org.heyner.excelutils.analyzetrx;

import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.ApachePoiConfigurer;
import org.heyner.excelutils.TestInitializerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {AnalyzeTRX.class, AnalyzeTRXConfig.class, ApachePoiConfigurer.class})
@EnableConfigurationProperties(AnalyzeTRXConfig.class)
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnalyzeTRXTest {
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_PCB_PROJ_TRX_03_1265199083.xlsx";
    @Autowired
    private AnalyzeTRX analyzeTRX;

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
        String [] args = {"analyzetrx", fileName};
        analyzeTRX.execute(args);
    }

    @Test
    void getCommandName() {
        assertEquals("analyzetrx", analyzeTRX.getCommandName());
    }

    @Test
    void execute() throws IOException {
        String filePath = "target/temp-"+this.getClass().getSimpleName()+"/300000000073327-Analyze TRX.xlsx";
        Path path = Paths.get(filePath);
        assertTrue(Files.exists(path));

        ExcelFile excelFile = ExcelFile.open(filePath);
        assertEquals(56, excelFile.rowCount("Datas",0));
    }
}