package org.heyner.excelutils.formatinvregisterln;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = FormatInvRegisterLnConfig.class)
@EnableConfigurationProperties(FormatInvRegisterLnConfig.class)
class FormatInvRegisterLnConfigTest {

    @Autowired
    @Qualifier("formatInvRegisterLnConfig")
    private FormatInvRegisterLnConfig config;

    @Test
    void testConfigurationLoading() {

        System.out.println("lastcolumn = " + config.getLastcolumn());
        System.out.println("nohidecolumns = " + config.getNohidecolumns());

        assertEquals(59, config.getLastcolumn());
        assertTrue(config.getNohidecolumns().contains(34));
    }
}
