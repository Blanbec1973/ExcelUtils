
package org.heyner.excelutils.analyzetrx;

import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.ApachePoiConfigurer;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = {
                AnalyzeTRX.class,
                AnalyzeTRXConfig.class,
                ApachePoiConfigurer.class,
                ModelClonerImpl.class,
                TrxDataTransfer.class,
                AnalyzeTrxIntegrationTest.TestConfig.class
        }
)
@ActiveProfiles("catalog-it") // <-- active uniquement pour CET IT
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // (optionnel, par sécurité)
@EnableConfigurationProperties(AnalyzeTRXConfig.class)
// On fixe les chemins pour ce test d'intégration
@TestPropertySource(properties = {
        "analyzetrx.sheetIn=sheet1",
        "analyzetrx.sheetOut=Datas",
        "analyzetrx.pathModel=src/test/resources/model/model Analyze TRX.xlsx",
        // Pas de placeholder de date pour rester déterministe
        "analyzetrx.pathResultFile=target/temp-AnalyzeTRXIT/Analyze TRX.xlsx"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnalyzeTrxIntegrationTest {

    @TestConfiguration
    @Profile("catalog-it")
    static class TestConfig {
        // Expander identité pour stabiliser le nom du fichier
        @Bean
        DateTemplateExpander dataTemplateExpander() {
            return template -> template;
        }
    }

    @SpyBean private ResultNamer resultNamer;      // on vérifie que c'est bien appelé
    @MockBean private PrefixReader prefixReader; // pour éviter d'ouvrir Excel dans le renamer
    @MockBean private FsRenamePort fsRenamer;           // on évite de renommer sur le FS

    @org.springframework.beans.factory.annotation.Autowired
    private AnalyzeTRX analyzeTRX;

    @BeforeEach
    void setupFs() throws Exception {
        // Copie les fixtures dans target/temp-AnalyzeTRXIT/...
        org.heyner.excelutils.TestInitializerFactory.action("AnalyzeTRXIT");
        // Prépare le prefix lu par ResultNamer
        when(prefixReader.read(anyString(), anyString(), anyString()))
                .thenReturn("300000000073327");
    }

    @Test
    void end_to_end_transfers_and_calls_rename() throws Exception {
        // Arrange
        String input = "target/temp-AnalyzeTRXIT/UC_PCB_PROJ_TRX_03_1265199083.xlsx";

        // Act
        analyzeTRX.execute("analyzetrx", input);

        // Assert : le fichier résultat existe
        Path out = Paths.get("target/temp-AnalyzeTRXIT/Analyze TRX.xlsx");
        assertTrue(Files.exists(out), "Le fichier résultat doit exister");

        // Assert : rowCount attendu sur la feuille Datas
        try (ExcelFile excel = ExcelFile.open(out.toString())) {
            assertEquals(56, excel.rowCount(ExcelConstants.DATAS_SHEET, 0));
        }

        // Assert : ResultNamer a bien été invoqué avec les bons paramètres
        verify(resultNamer).renameIfNeeded(
                argThat(p -> Paths.get(p).endsWith("Analyze TRX.xlsx")),
                eq(ExcelConstants.DATAS_SHEET),
                eq(ExcelConstants.TRX_CONTRACT_CELL)
        );
    }
}


/*
@SpringBootTest(classes = {AnalyzeTRX.class, AnalyzeTRXConfig.class, ApachePoiConfigurer.class})
@EnableConfigurationProperties(AnalyzeTRXConfig.class)
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnalyzeTrxIntegrationTest {
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
}*/
