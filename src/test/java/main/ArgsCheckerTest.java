package main;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.heyner.common.Parameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ArgsCheckerTest {
    private final Parameter param = new Parameter("config.properties");

    @Test
    @ExpectSystemExitWithStatus(-1)
    void testArgumentNull() {
        String [] tabNull={};
        new ArgsChecker(tabNull,null);
        assertTrue(true);
    }

    @Test
    @ExpectSystemExitWithStatus(-1)
    void testInvalidFunction() {
        String [] tab1 = {"INVALID"};
        new ArgsChecker(tab1, null);
        assertTrue(true);
    }

    @Test
    @ExpectSystemExitWithStatus(-1)
    void testInvalidNumberOfArguments() {
        String [] tab1 = {"FUSION_TRX"};
        new ArgsChecker(tab1,param);
        assertTrue(true);
    }

    @Test
    void testValidArgument() {
        String [] tab1 = {"FUSION_TRX", "1", "2"};
        ArgsChecker argsChecker = new ArgsChecker(tab1,param);
        assertTrue(argsChecker.isValid());
    }

}