CREATE TABLE teacher_preferences (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    teacher_id INTEGER NOT NULL,
    course_id INTEGER,
    group_id INTEGER,
    study_year INTEGER,
    day_of_week VARCHAR(20) NOT NULL,
    priority INTEGER,
    start_hour INTEGER,
    end_hour INTEGER,
    preference_type VARCHAR(20),
    FOREIGN KEY (teacher_id) REFERENCES teacher_profiles(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (group_id) REFERENCES student_groups(id)
);

CREATE INDEX ix_tp_teacher ON teacher_preferences(teacher_id);
