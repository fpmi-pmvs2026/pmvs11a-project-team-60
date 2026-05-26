package stanulpych.pmvs.labtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [LabEntryEntity::class], version = 1, exportSchema = false)
abstract class LabDatabase : RoomDatabase() {
    abstract fun labDao(): LabDao

    companion object {
        @Volatile
        private var instance: LabDatabase? = null

        fun get(context: Context): LabDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    LabDatabase::class.java,
                    "labtracker.db",
                ).build().also { instance = it }
            }

        suspend fun seedIfEmpty(context: Context) {
            val dao = get(context).labDao()
            if (dao.count() > 0) return
            val seed = listOf(
                Triple(5, "Жесты и калькулятор", "lab5-pmvs"),
                Triple(6, "Хранение данных Android", "lab6-pmvs"),
                Triple(7, "Aurora / Qt (Android)", "lab7-pmvs"),
                Triple(8, "Flutter", "lab8-pmvs"),
                Triple(9, "Кроссплатформа", "lab9-pmvs"),
                Triple(10, "IoT Node-RED", "lab10-pmvs"),
            )
            seed.forEach { (num, title, folder) ->
                dao.upsert(
                    LabEntryEntity(
                        labNumber = num,
                        title = title,
                        status = if (num <= 8) LabEntryEntity.STATUS_DONE else LabEntryEntity.STATUS_IN_PROGRESS,
                        repoUrl = "https://github.com/stanulpych/$folder",
                        notes = "Демо-данные",
                    ),
                )
            }
        }
    }
}
