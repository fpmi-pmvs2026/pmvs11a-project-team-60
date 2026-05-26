package stanulpych.pmvs.labtracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import stanulpych.pmvs.labtracker.data.LabDatabase
import stanulpych.pmvs.labtracker.data.LabEntryEntity
import stanulpych.pmvs.labtracker.data.LabRepository
import stanulpych.pmvs.labtracker.domain.StatusRules

class LabViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = LabRepository(LabDatabase.get(app).labDao())

    init {
        viewModelScope.launch { LabDatabase.seedIfEmpty(app) }
    }

    val labs = repo.observeLabs().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList(),
    )

    fun save(entry: LabEntryEntity) {
        viewModelScope.launch { repo.save(entry) }
    }

    fun delete(id: Long) {
        viewModelScope.launch { repo.delete(id) }
    }

    fun cycleStatus(current: String): String = StatusRules.cycle(current)

    fun progressText(list: List<LabEntryEntity>): String =
        "Прогресс: ${repo.progressSummary(list)}"
}
