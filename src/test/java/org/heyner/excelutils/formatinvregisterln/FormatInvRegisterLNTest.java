package org.heyner.excelutils.formatinvregisterln;

import org.heyner.common.ExcelFile;
import org.heyner.excelutils.ApachePoiConfigurer;
import org.heyner.excelutils.TestInitializerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {FormatInvRegisterLN.class, ApachePoiConfigurer.class})
@EnableConfigurationProperties(FormatInvRegisterLnConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormatInvRegisterLNTest {
    @Autowired
    private FormatInvRegisterLN formatInvRegisterLN;
    private final String fileName = "target/temp-"+this.getClass().getSimpleName()+"/UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx";
    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }
    @Test
    void execute() throws IOException {
        formatInvRegisterLN.execute(fileName);

        ExcelFile fichierExcel = new ExcelFile(fileName);
        assertEquals("Entité",fichierExcel.getCellValue("sheet1",0,0));
        assertEquals(25, fichierExcel.rowCount("sheet1",0));
    }

    @Test
    void getCommandName() {
        assertEquals("formatinvregisterln",formatInvRegisterLN.getCommandName());
    }
}