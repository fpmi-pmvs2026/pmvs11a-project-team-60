# Презентация проекта

## Материалы

- [Текст слайдов (Markdown)](presentation/slides.md)
- PDF можно собрать из `slides.md` (Pandoc / Marp) и положить в `presentation/presentation.pdf`

## Краткое содержание

1. **Цель** — персональный трекер лабораторных ПМиВС на Android.
2. **Use Case** — список лаб, статусы, дедлайны, напоминания, экспорт.
3. **Архитектура** — MVVM + Clean: `ui`, `domain`, `data`; Hilt, Room, WorkManager.
4. **БД** — `semesters`, `lab_entries`, `reminders`.
5. **Команда и CI** — submodule `code`, GitHub Actions (assemble + test).
6. **Итоги** — офлайн-прогресс, уведомления; планы: виджет, синхронизация с GitHub API.
