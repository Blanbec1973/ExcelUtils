
package org.heyner.excelutils.directoryparser;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileClassifierTest {

    @Test
    void shouldClassifyActivity() {
        FileClassifier c = new FileClassifier();
        assertEquals(FileType.ACTIVITY,
                c.classify(Path.of(".../UC_AR_ITEM_ACTIVITY_V1_...xlsx")));
    }

    @Test
    void shouldClassifyTrx() {
        FileClassifier c = new FileClassifier();
        assertEquals(FileType.TRX,
                c.classify(Path.of(".../UC_PCB_PROJ_TRX_...xlsx")));
    }

    @Test
    void shouldClassifyInvRegisterLn() {
        FileClassifier c = new FileClassifier();
        assertEquals(FileType.INV_REGISTER_LN,
                c.classify(Path.of(".../UC_PCB_MS_INV_REGISTER_LN_...xlsx")));
    }

    @Test
    void shouldReturnUnknown() {
        FileClassifier c = new FileClassifier();
        assertEquals(FileType.UNKNOWN, c.classify(Path.of(".../other.xlsx")));
    }
}