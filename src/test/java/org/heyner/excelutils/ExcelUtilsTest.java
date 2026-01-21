
package org.heyner.excelutils;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(
        classes = {ExcelUtils.class},   // on démarre l'app Spring Boot réelle
        args = {"test", "arg1"}         // on passe une commande "test"
)
class ExcelUtilsTest {

    // Remplace le vrai ArgsChecker par un mock
    @MockitoBean
    private ArgsChecker argsChecker;

    // On ajoute un CommandService "test" mocké pour satisfaire le dispatcher
    @MockitoBean(name = "testCommandService")
    private CommandService testCommand;

    @Test
    void contextLoads() throws Exception {
        // Arrange: on prépare les mocks
        given(argsChecker.validate(Mockito.any())).willReturn(true);
        given(testCommand.getCommandName()).willReturn("test");
        doNothing().when(testCommand).execute(Mockito.any());

        // Act: rien à faire, @SpringBootTest démarre le contexte
        // et CommandDispatcher (CommandLineRunner) s’exécute

        // Assert: si on arrive ici sans exception, le contexte a démarré.
        // Tu peux ajouter des vérifications si tu veux, p.ex. :
        Mockito.verify(testCommand, Mockito.atLeastOnce()).getCommandName();
    }
}
