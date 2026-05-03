package org.heyner.excelutils.application.commands.correctionimputation;

import org.springframework.stereotype.Component;

@Component
public class CorrectionImputationRule {
    //8 : code   476867
    //9 : nom    POMMERET

    public boolean shouldCorrect(String cell56Value, String code8Value) {
        return "-Difficulté à déterminer-".equals(cell56Value)
                && "476867".equals(code8Value);
    }

    public String formulaFor(int rowNum) {
        return "AC" + rowNum + "/8";
    }
}
