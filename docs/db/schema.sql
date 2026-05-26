PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS semesters (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    year INTEGER NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS lab_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    semester_id INTEGER,
    lab_number INTEGER NOT NULL,
    title TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'planned',
    repo_url TEXT,
    notes TEXT,
    deadline_epoch INTEGER,
    sort_index INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL,
    UNIQUE (semester_id, lab_number),
    FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS reminders (
    lab_id INTEGER PRIMARY KEY,
    fire_at_epoch INTEGER NOT NULL,
    enabled INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (lab_id) REFERENCES lab_entries(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS index_lab_entries_status ON lab_entries(status);
CREATE INDEX IF NOT EXISTS index_lab_entries_semester ON lab_entries(semester_id);
