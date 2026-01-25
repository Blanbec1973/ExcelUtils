
package org.heyner.excelutils;

import org.heyner.excelutils.utils.DateTemplateExpander;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;



@SpringBootTest(
        classes = {ExcelUtils.class},
        args = {"test", "arg1"}
)
class ExcelUtilsTest {
    @MockitoBean
    private ArgsChecker argsChecker;
    @MockitoBean
    private ApplicationProperties applicationProperties;
    @MockitoBean
    private DateTemplateExpander dateTemplateExpander;

    @BeforeEach
    void setUp() {
        given(argsChecker.validateOrThrow(any())).willReturn(true);
        given(applicationProperties.getProjectName()).willReturn("ExcelUtils");
        given(applicationProperties.getVersion()).willReturn("Test");
        given(dateTemplateExpander.expand(any()))
                .willAnswer(inv -> inv.getArgument(0));
    }

//    TODO régler tests
    @Test
    void contextLoads() {
        assertTrue(true);
    }
}



