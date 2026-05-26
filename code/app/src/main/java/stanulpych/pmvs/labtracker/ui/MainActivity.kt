package stanulpych.pmvs.labtracker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import stanulpych.pmvs.labtracker.data.LabEntryEntity
import stanulpych.pmvs.labtracker.domain.StatusRules

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabTrackerApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabTrackerApp(vm: LabViewModel = viewModel()) {
    val labs by vm.labs.collectAsState()
    val context = LocalContext.current
    var showAdd by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<LabEntryEntity?>(null) }

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("PmvsLabTracker") })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAdd = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            },
        ) { padding ->
            Column(Modifier.padding(padding)) {
                Text(
                    vm.progressText(labs),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(labs, key = { it.id }) { lab ->
                        LabCard(
                            lab = lab,
                            onStatusClick = { vm.save(lab.copy(status = vm.cycleStatus(lab.status))) },
                            onOpenRepo = { openUrl(context, lab.repoUrl) },
                            onEdit = { editTarget = lab },
                            onDelete = { vm.delete(lab.id) },
                        )
                    }
                }
            }
        }
    }

    if (showAdd) {
        LabEditDialog(
            title = "Новая лабораторная",
            initial = LabEntryEntity(labNumber = (labs.maxOfOrNull { it.labNumber } ?: 4) + 1, title = ""),
            onDismiss = { showAdd = false },
            onSave = {
                vm.save(it)
                showAdd = false
            },
        )
    }

    editTarget?.let { lab ->
        LabEditDialog(
            title = "Редактирование ЛР${lab.labNumber}",
            initial = lab,
            onDismiss = { editTarget = null },
            onSave = {
                vm.save(it)
                editTarget = null
            },
        )
    }
}

@Composable
private fun LabCard(
    lab: LabEntryEntity,
    onStatusClick: () -> Unit,
    onOpenRepo: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onEdit)) {
        Column(Modifier.padding(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("ЛР${lab.labNumber}: ${lab.title}", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                }
            }
            Text("Статус: ${statusLabel(lab.status)}", modifier = Modifier.clickable { onStatusClick() })
            if (lab.repoUrl.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(lab.repoUrl, modifier = Modifier.weight(1f), maxLines = 1)
                    IconButton(onClick = onOpenRepo) {
                        Icon(Icons.Default.OpenInNew, contentDescription = "Открыть")
                    }
                }
            }
            if (lab.notes.isNotBlank()) {
                Text("Заметка: ${lab.notes}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun LabEditDialog(
    title: String,
    initial: LabEntryEntity,
    onDismiss: () -> Unit,
    onSave: (LabEntryEntity) -> Unit,
) {
    var number by remember { mutableStateOf(initial.labNumber.toString()) }
    var labTitle by remember { mutableStateOf(initial.title) }
    var url by remember { mutableStateOf(initial.repoUrl) }
    var notes by remember { mutableStateOf(initial.notes) }
    var status by remember { mutableStateOf(initial.status) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(number, { number = it }, label = { Text("Номер ЛР") }, singleLine = true)
                OutlinedTextField(labTitle, { labTitle = it }, label = { Text("Название") })
                OutlinedTextField(url, { url = it }, label = { Text("URL репозитория") })
                OutlinedTextField(notes, { notes = it }, label = { Text("Заметка") })
                TextButton(onClick = { status = StatusRules.cycle(status) }) {
                    Text("Статус: ${statusLabel(status)} (нажмите для смены)")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val num = number.toIntOrNull() ?: return@TextButton
                if (labTitle.isBlank()) return@TextButton
                onSave(
                    initial.copy(
                        labNumber = num,
                        title = labTitle.trim(),
                        repoUrl = url.trim(),
                        notes = notes.trim(),
                        status = status,
                    ),
                )
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } },
    )
}

private fun statusLabel(status: String): String = StatusRules.label(status)

private fun openUrl(context: android.content.Context, url: String) {
    if (url.isBlank()) return
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.onFailure { println("Open URL error: ${it.message}") }
}
