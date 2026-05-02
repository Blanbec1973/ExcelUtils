package org.heyner.excelutils.infrastructure.excel;

import lombok.RequiredArgsConstructor;
import org.heyner.excelutils.application.commands.analyzetrx.TrxDataTransfer;
import org.heyner.excelutils.application.ports.ExcelTransferPort;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ExcelTransferAdapter implements ExcelTransferPort {

    private final TrxDataTransfer delegate;

    @Override
    public int transfer(Path input, Path output, String sheetIn, String sheetOut) {
        return delegate.transfer(input, output, sheetIn, sheetOut);
    }
}
