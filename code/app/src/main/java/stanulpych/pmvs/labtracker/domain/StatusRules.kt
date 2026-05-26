package stanulpych.pmvs.labtracker.domain

import stanulpych.pmvs.labtracker.data.LabEntryEntity

object StatusRules {
    fun cycle(current: String): String = when (current) {
        LabEntryEntity.STATUS_PLANNED -> LabEntryEntity.STATUS_IN_PROGRESS
        LabEntryEntity.STATUS_IN_PROGRESS -> LabEntryEntity.STATUS_DONE
        else -> LabEntryEntity.STATUS_PLANNED
    }

    fun label(status: String): String = when (status) {
        LabEntryEntity.STATUS_IN_PROGRESS -> "в работе"
        LabEntryEntity.STATUS_DONE -> "сдано"
        else -> "запланировано"
    }
}
