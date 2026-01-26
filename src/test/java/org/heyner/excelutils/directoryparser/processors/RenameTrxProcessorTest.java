package org.heyner.excelutils.directoryparser.processors;

import org.heyner.excelutils.directoryparser.FileClassifier;
import org.heyner.excelutils.utils.filenaming.ResultNamer;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.mock;

class RenameTrxProcessorTest
        extends AbstractFileProcessorContractTest<RenameTrxProcessor> {

        @Override
        protected RenameTrxProcessor newProcessor() {
            return new RenameTrxProcessor(new FileClassifier(), mock(ResultNamer.class));
        }

        @Override
        protected List<Path> matchingSamples() {
            return List.of(
                    Path.of(".../UC_PCB_PROJ_TRX_03_1265199083.xlsx"),
                    Path.of(".../UC_PCB_PROJ_TRX_03_834070930.xlsx")

            );
        }

        @Override
        protected List<Path> nonMatchingSamples() {
            return List.of(
                    Path.of(".../UC_AR_ITEM_ACTIVITY_V1_03.xlsx"),
                    Path.of(".../300000000073327-UC_AR_ITEM_ACTIVITY_V1_03_1790667600.xlsx")
            );
        }
    }