package org.heyner.excelutils.application.commands.correctionimputation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectionImputationRuleTest {

    private final CorrectionImputationRule rule = new CorrectionImputationRule();

    @Test
    void shouldCorrect_returnsTrue_whenCell56IsDifficulteAndCode8Is476867() {
        // Given
        String cell56Value = "-Difficulté à déterminer-";
        String code8Value = "476867";

        // When
        boolean result = rule.shouldCorrect(cell56Value, code8Value);

        // Then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "'-Difficulté à déterminer-', '123456'",  // code8 différent
        "'Autre valeur', '476867'",               // cell56 différent
        "'Autre valeur', '123456'",               // les deux différents
        "'', '476867'",                           // cell56 vide
        "'-Difficulté à déterminer-', ''",        // code8 vide
        "'null', '476867'",                       // cell56 null string
        "'-Difficulté à déterminer-', 'null'"     // code8 null string
    })
    void shouldCorrect_returnsFalse_whenConditionsNotMet(String cell56Value, String code8Value) {
        // When
        boolean result = rule.shouldCorrect(cell56Value, code8Value);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldCorrect_returnsFalse_whenCell56IsNull() {
        // When
        boolean result = rule.shouldCorrect(null, "476867");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldCorrect_returnsFalse_whenCode8IsNull() {
        // When
        boolean result = rule.shouldCorrect("-Difficulté à déterminer-", null);

        // Then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
        "1, AC1/8",
        "10, AC10/8",
        "100, AC100/8",
        "999, AC999/8"
    })
    void formulaFor_returnsCorrectFormula(int rowNum, String expectedFormula) {
        // When
        String result = rule.formulaFor(rowNum);

        // Then
        assertThat(result).isEqualTo(expectedFormula);
    }
}