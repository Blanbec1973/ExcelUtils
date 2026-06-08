package org.heyner.excelutils.application.commands.directoryparser.pipeline;

import org.heyner.excelutils.shared.constants.ExcelConstants;
import org.heyner.excelutils.shared.util.filenaming.ResultNamer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenameRegisterLnProcessorTest
        extends AbstractFileProcessorContractTest<RenameRegisterLnProcessor> {

    private final ResultNamer renamer = mock(ResultNamer.class);

    @Override
    protected RenameRegisterLnProcessor newProcessor() {
        return new RenameRegisterLnProcessor(renamer);
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

    @Test
    void processDelegatesRenameToResultNamer() {
        Path file = Path.of("UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx");
        RenameRegisterLnProcessor processor = newProcessor();

        processor.process(file);

        verify(renamer, times(1)).renameIfNeeded(file, ExcelConstants.DEFAULT_SHEET, ExcelConstants.INV_REGISTER_LN_CONTRACT_CELL);
    }

    @Test
    void processPassesExactFilePathToRenamer() {
        Path file = Path.of("some", "dir", "UC_PCB_MS_INV_REGISTER_LN_03_834070930.xlsx");
        RenameRegisterLnProcessor processor = newProcessor();

        processor.process(file);

        verify(renamer).renameIfNeeded(eq(file), anyString(), anyString());
    }
}

