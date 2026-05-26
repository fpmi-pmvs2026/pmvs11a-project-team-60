# PmvsLabTracker — Android

Полноценное Android-приложение (Jetpack Compose + Room).

## Запуск

```bash
cd code
chmod +x gradlew
./gradlew installDebug
```

Или из корня проекта:

```bash
./scripts/run-android.sh
```

`applicationId`: `stanulpych.pmvs.labtracker`

## Возможности

- список лабораторных ЛР5–ЛР10 (демо-данные при первом запуске);
- смена статуса по нажатию на строку статуса;
- добавление / редактирование / удаление;
- открытие ссылки на репозиторий в браузере;
- индикатор прогресса (% сданных).

## Тесты

```bash
./gradlew testDebugUnitTest
```
