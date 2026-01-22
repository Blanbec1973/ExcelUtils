package org.heyner.excelutils.directoryparser.processors;


import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.directoryparser.FileRenamer;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.mock;

class RenameActivityProcessorTest
        extends AbstractFileProcessorContractTest<RenameActivityProcessor> {

    @Override
    protected RenameActivityProcessor newProcessor() {
        return new RenameActivityProcessor(new FileClassifier(), mock(FileRenamer.class));
    }

    @Override
    protected List<File> matchingSamples() {
        return List.of(
                new File(".../UC_AR_ITEM_ACTIVITY_V1_03.xlsx"),
                new File(".../300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx")
        );
    }

    @Override
    protected List<File> nonMatchingSamples() {
        return List.of(
                new File(".../UC_PCB_PROJ_TRX_03_1265199083.xlsx"),
                new File(".../UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx")
        );
    }
}
