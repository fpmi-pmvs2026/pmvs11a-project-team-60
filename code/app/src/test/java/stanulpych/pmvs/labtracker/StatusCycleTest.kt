package stanulpych.pmvs.labtracker

import org.junit.Assert.assertEquals
import org.junit.Test
import stanulpych.pmvs.labtracker.data.LabEntryEntity
import stanulpych.pmvs.labtracker.domain.StatusRules

class StatusCycleTest {
    @Test
    fun cyclesThreeStates() {
        assertEquals(LabEntryEntity.STATUS_IN_PROGRESS, StatusRules.cycle(LabEntryEntity.STATUS_PLANNED))
        assertEquals(LabEntryEntity.STATUS_DONE, StatusRules.cycle(LabEntryEntity.STATUS_IN_PROGRESS))
        assertEquals(LabEntryEntity.STATUS_PLANNED, StatusRules.cycle(LabEntryEntity.STATUS_DONE))
    }
}
