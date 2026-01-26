package org.heyner.excelutils.directoryparser.processors;

import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.formatinvregisterln.FormatInvRegisterLN;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.mock;

class FormatInvRegisterLnProcessorTest
       extends AbstractFileProcessorContractTest<FormatInvRegisterLnProcessor> {

    @Override
    protected FormatInvRegisterLnProcessor newProcessor() {
        return new FormatInvRegisterLnProcessor(new FileClassifier(), mock(FormatInvRegisterLN.class));
    }

    @Override
    protected List<Path> matchingSamples() {
        return List.of(
                Path.of(".../UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx"),
                Path.of(".../300000000073327-UC_PCB_MS_INV_REGISTER_LN_03_1790667600.xlsx")
        );
    }

    @Override
    protected List<Path> nonMatchingSamples() {
        return List.of(
                Path.of(".../UC_PCB_PROJ_TRX_03_1265199083.xlsx"),
                Path.of(".../UC_AR_ITEM_ACTIVITY_V1_03.xlsx")
        );
    }
}