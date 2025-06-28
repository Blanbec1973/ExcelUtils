package org.heyner.excelutils;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ArgsChecker.class,CommandProperties.class})
@EnableConfigurationProperties(CommandProperties.class)
@TestPropertySource("classpath:application.properties")
class ArgsCheckerTest {

    @Autowired
    private ArgsChecker argsChecker;

    @Test
    @ExpectSystemExitWithStatus(-1)
    void testArgumentNull() {
        String [] tabNull={};
        argsChecker.validate(tabNull);
        assertTrue(true);
    }

    @Test
    @ExpectSystemExitWithStatus(-1)
    void testInvalidFunction() {
        String [] tab1 = {"INVALID"};
        argsChecker.validate(tab1);
        assertTrue(true);
    }

    @Test
    @ExpectSystemExitWithStatus(-1)
    void testInvalidNumberOfArguments() {
        String [] tab1 = {"fusiontrx"};
        assertFalse(argsChecker.validate(tab1));
    }

    @Test
    void testValidArgument() {
        String [] tab1 = {"fusiontrx", "1"};
        assertTrue( argsChecker.validate(tab1));
    }

}