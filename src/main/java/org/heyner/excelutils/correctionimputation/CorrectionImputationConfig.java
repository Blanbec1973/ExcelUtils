package org.heyner.excelutils.correctionimputation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorrectionImputationConfig {

    @Value("${correctionimputation.action:false}")
    private boolean correctionImputationAction;

    public boolean isCorrectionImputationActionEnabled() {
        return correctionImputationAction;
    }
}

