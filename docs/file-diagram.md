# Диаграмма структуры проекта

## Диаграмма

```mermaid
flowchart TD
    ROOT[pmvs-labtracker-project_stanulpych]
    ROOT --> DOCS[docs]
    ROOT --> WIKI[wiki]
    ROOT --> CODE[code submodule]
    ROOT --> README[README.md]

    CODE --> APP[app/src/main/java/stanulpych/pmvs/labtracker]
    APP --> UI[ui: Compose screens]
    APP --> DOM[domain]
    APP --> DATA[data]
    APP --> CORE[core: utils, dispatchers]

    DATA --> LOCAL[local: Room, DAO, entities]
    DATA --> REPO[repository impl]
    DATA --> WORK[work: DeadlineReminderWorker]
    DATA --> PREFS[preferences: DataStore]

    DOM --> MODEL[model: LabEntry, Semester]
    DOM --> UC[usecase]
    DOM --> PORT[repository ports]

    UI --> LIST[LabListScreen]
    UI --> DETAIL[LabDetailScreen]
    UI --> SETTINGS[SettingsScreen]
    UI --> NAV[navigation: NavHost]
```

## Ключевые каталоги

| Путь | Назначение |
|------|------------|
| `docs/` | Документация для GitHub Pages |
| `wiki/` | Страницы для GitHub Wiki (импорт) |
| `code/` | Android-приложение (git submodule) |
| `code/.../ui` | Jetpack Compose, ViewModel |
| `code/.../domain` | Бизнес-правила, use case |
| `code/.../data` | Room, WorkManager, DataStore |
