package org.heyner.excelutils.infrastructure.excel;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationRule;
import org.heyner.excelutils.infrastructure.config.CorrectionImputationConfig;
import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class CorrectionImputationAdapterTest {
    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("target/test/correctionImputation");
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File("target/test/correctionImputation"));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"),
                                new File("target/test/correctionImputation/"));
    }

    @Test
    void testCorrectionImputationAdapter() throws IOException {

        CorrectionImputationConfig correctionImputationConfig = mock(CorrectionImputationConfig.class);
        when(correctionImputationConfig.isCorrectionImputationActionEnabled()).thenReturn(true);

        CorrectionImputationAdapter adapter = new CorrectionImputationAdapter(new CorrectionImputationRule());

        adapter.correct(Path.of("target/test/correctionImputation/TrxToCorrect.xlsx"),
                ExcelConstants.DEFAULT_SHEET);

        ExcelFile fichierExcel = ExcelFile.open("target/test/correctionImputation/TrxToCorrect.xlsx");
        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(ExcelConstants.DEFAULT_SHEET);
        String formula = dataSheet.getRow(10).getCell(56).getCellFormula();
        assertEquals("AC11/8", formula);
        fichierExcel.close();
    }

}