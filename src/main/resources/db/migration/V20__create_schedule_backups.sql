-- V20: Create schedule_backups table to archive old schedules
CREATE TABLE schedule_backups (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    original_entry_id INTEGER,
    offering_id INTEGER REFERENCES course_offerings(id),
    group_id INTEGER,
    timeslot_id INTEGER REFERENCES timeslots(id),
    room_id INTEGER REFERENCES rooms(id),
    week_pattern TEXT,
    status TEXT,
    created_at TIMESTAMP,
    backup_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ix_sb_original_id ON schedule_backups(original_entry_id);
CREATE INDEX ix_sb_group ON schedule_backups(group_id);
CREATE INDEX ix_sb_backup_ts ON schedule_backups(backup_timestamp);