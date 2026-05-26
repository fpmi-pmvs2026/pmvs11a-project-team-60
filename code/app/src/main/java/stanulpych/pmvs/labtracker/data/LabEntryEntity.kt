package stanulpych.pmvs.labtracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lab_entries",
    indices = [Index(value = ["labNumber"], unique = true)],
)
data class LabEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val labNumber: Int,
    val title: String,
    val status: String = STATUS_PLANNED,
    val repoUrl: String = "",
    val notes: String = "",
    val deadlineEpoch: Long? = null,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    companion object {
        const val STATUS_PLANNED = "planned"
        const val STATUS_IN_PROGRESS = "in_progress"
        const val STATUS_DONE = "done"
    }
}
