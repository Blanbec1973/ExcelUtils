package org.heyner.excelutils.fusiontrx;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.TestInitializerFactory;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class FusionProcessorTest {

    @BeforeEach
    void setUp() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
        File f = new File ("target/temp-FusionProcessorTest/fusion/FusionTRX.xlsx");
        if (f.exists())
            f.delete();
    }

    @Test
    void testFusionTRX() throws IOException {
        FusionProcessor fusionProcessor=new FusionProcessor();
        fusionProcessor.process( "target/temp-FusionProcessorTest/fusion/",
                                                        "target/temp-FusionProcessorTest/fusion/");

        ExcelFile fichierExcel = ExcelFile.open("target/temp-FusionProcessorTest/fusion/FusionTRX.xlsx");

        Sheet dataSheet = fichierExcel.getWorkBook().getSheet(ExcelConstants.FUSION_SHEET);
        double foreignAmountCum = StreamSupport.stream(dataSheet.spliterator(), false)
                .skip(1)
                .map(row -> row.getCell(ExcelConstants.FOREIGN_AMOUNT_COLUMN))
                .filter(Objects::nonNull)
                .mapToDouble(Cell::getNumericCellValue)
                        .sum();

        int rowCount = dataSheet.getLastRowNum() + 1; // getLastRowNum est 0-based
        assertEquals(12, rowCount);
        assertEquals(630260,Math.round(100*foreignAmountCum)/100);
    }
    @Test
    void shouldThrowGracefulExitExceptionWhenNoTrxFilesFound() {
        // Arrange : créer un dossier vide
        File emptyDir = new File("target/temp-FusionProcessorTest/empty");
        if (!emptyDir.exists()) {
            assertTrue(emptyDir.mkdirs(), "Échec de la création du dossier de test.");
        }

        FusionProcessor fusionProcessor = new FusionProcessor();
        String emptyDirAboslutePath = emptyDir.getAbsolutePath();
        // Act & Assert
        GracefulExitException exception = assertThrows(
                GracefulExitException.class,
                () -> fusionProcessor.process(emptyDirAboslutePath, "target/test/fusiontrx/output/")
        );

        assertTrue(exception.getMessage().contains("No file to process"));
        assertEquals(0, exception.getExitCode());
    }

}