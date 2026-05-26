package stanulpych.pmvs.labtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LabDao {
    @Query("SELECT * FROM lab_entries ORDER BY labNumber ASC")
    fun observeAll(): Flow<List<LabEntryEntity>>

    @Query("SELECT * FROM lab_entries WHERE id = :id")
    suspend fun getById(id: Long): LabEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: LabEntryEntity): Long

    @Update
    suspend fun update(entry: LabEntryEntity)

    @Query("DELETE FROM lab_entries WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM lab_entries")
    suspend fun count(): Int
}
