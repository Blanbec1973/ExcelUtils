package org.heyner.excelutils.infrastructure.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.application.commands.correctionimputation.CorrectionImputationService;
import org.heyner.excelutils.application.ports.CorrectionImputationPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CorrectionImputationAdapter implements CorrectionImputationPort {

    private final CorrectionImputationService delegate;

    @Override
    public void processRow(Row row, ExcelFile fichierExcel) {
        delegate.processRow(row, fichierExcel);
    }
}