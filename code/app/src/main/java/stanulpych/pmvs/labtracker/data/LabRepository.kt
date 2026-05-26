package stanulpych.pmvs.labtracker.data

import kotlinx.coroutines.flow.Flow

class LabRepository(private val dao: LabDao) {
    fun observeLabs(): Flow<List<LabEntryEntity>> = dao.observeAll()

    suspend fun get(id: Long): LabEntryEntity? = dao.getById(id)

    suspend fun save(entry: LabEntryEntity) {
        if (entry.id == 0L) {
            dao.upsert(entry.copy(updatedAt = System.currentTimeMillis()))
        } else {
            dao.update(entry.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun delete(id: Long) = dao.delete(id)

    fun progressSummary(list: List<LabEntryEntity>): String {
        if (list.isEmpty()) return "0%"
        val done = list.count { it.status == LabEntryEntity.STATUS_DONE }
        return "${done * 100 / list.size}%"
    }
}
