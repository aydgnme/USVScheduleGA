-- V1: Initialize normalized schema for SQLite (USV - Universitatea Ștefan cel Mare Suceava)
-- Create base tables for university scheduling system

-- Faculties (top-level organizational units)
CREATE TABLE faculties (
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    short_name TEXT,
    CONSTRAINT ck_faculty_code CHECK (code IN ('FDSA', 'SEAB', 'FEFS', 'FIA', 'FIESC', 'FIM', 'FIG', 'FLSC', 'FMSB', 'FSED', 'SILVIC'))
);

-- Departments (within faculties)
CREATE TABLE departments (
    id INTEGER PRIMARY KEY,
    faculty_id INTEGER NOT NULL REFERENCES faculties(id),
    code TEXT NOT NULL,
    name TEXT NOT NULL,
    CONSTRAINT uk_dept_code UNIQUE (faculty_id, code)
);

CREATE INDEX ix_dept_faculty ON departments(faculty_id);

-- Specializations (study programs within departments)
CREATE TABLE specializations (
    id INTEGER PRIMARY KEY,
    department_id INTEGER NOT NULL REFERENCES departments(id),
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    study_cycle TEXT NOT NULL,
    CONSTRAINT ck_spec_cycle CHECK (study_cycle IN ('BACHELOR', 'MASTER', 'DOCTORATE', 'CONVERSION'))
);

CREATE INDEX ix_spec_dept ON specializations(department_id);

-- Users table (authentication)
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    enabled INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT ck_role CHECK (role IN ('TEACHER', 'SECRETARY', 'ADMIN'))
);

CREATE INDEX uk_users_email ON users(email);

-- Teacher profiles
CREATE TABLE teacher_profiles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE REFERENCES users(id),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    max_hours_weekly INTEGER,
    available_days_json TEXT,
    preferred_time TEXT,
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_tp_user ON teacher_profiles(user_id);

-- Teacher departments (many-to-many: teachers can teach in multiple departments)
CREATE TABLE teacher_departments (
    teacher_id INTEGER NOT NULL REFERENCES teacher_profiles(id),
    department_id INTEGER NOT NULL REFERENCES departments(id),
    PRIMARY KEY (teacher_id, department_id)
);

CREATE INDEX ix_tdept_teacher ON teacher_departments(teacher_id);
CREATE INDEX ix_tdept_dept ON teacher_departments(department_id);

-- Groups (study groups/cohorts)
CREATE TABLE groups (
    id INTEGER PRIMARY KEY,
    specialization_id INTEGER NOT NULL REFERENCES specializations(id),
    code TEXT NOT NULL UNIQUE,
    study_year INTEGER NOT NULL,
    group_number INTEGER,
    subgroup_index TEXT,
    is_modular INTEGER DEFAULT 0,
    CONSTRAINT ck_year CHECK (study_year IN (0, 1, 2, 3, 4))
);

CREATE INDEX ix_grp_spec ON groups(specialization_id);

-- Courses (course definitions)
CREATE TABLE courses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL,
    component_type TEXT NOT NULL,
    credits INTEGER,
    semester INTEGER,
    parity TEXT NOT NULL,
    CONSTRAINT ck_comp_type CHECK (component_type IN ('LECTURE', 'LAB', 'SEMINAR', 'PROJECT')),
    CONSTRAINT ck_parity CHECK (parity IN ('BOTH', 'ODD', 'EVEN'))
);

CREATE INDEX uk_courses_code ON courses(code);

-- Rooms
CREATE TABLE rooms (
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    capacity INTEGER,
    room_type TEXT,
    building TEXT,
    computers INTEGER,
    features_json TEXT,
    CONSTRAINT ck_room_type CHECK (room_type IN ('LECTURE_HALL', 'LAB', 'SEMINAR_ROOM', 'MEETING', 'VIRTUAL', 'OTHER'))
);

CREATE INDEX uk_rooms_code ON rooms(code);
CREATE INDEX ix_rooms_building ON rooms(building);

-- Timeslots
CREATE TABLE timeslots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    day_of_week TEXT NOT NULL,
    start_time TEXT NOT NULL,
    end_time TEXT NOT NULL,
    duration_minutes INTEGER,
    CONSTRAINT ck_day CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'))
);

-- Course offerings (course assignments to groups and teachers)
CREATE TABLE course_offerings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    course_id INTEGER NOT NULL REFERENCES courses(id),
    group_id INTEGER NOT NULL REFERENCES groups(id),
    teacher_id INTEGER NOT NULL REFERENCES teacher_profiles(id),
    weekly_hours INTEGER,
    parity TEXT NOT NULL,
    CONSTRAINT ck_offering_parity CHECK (parity IN ('BOTH', 'ODD', 'EVEN'))
);

CREATE INDEX ix_co_course ON course_offerings(course_id);
CREATE INDEX ix_co_group ON course_offerings(group_id);
CREATE INDEX ix_co_teacher ON course_offerings(teacher_id);

-- Schedule entries (placement into timeslot and room)
CREATE TABLE schedule_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    offering_id INTEGER NOT NULL REFERENCES course_offerings(id),
    timeslot_id INTEGER NOT NULL REFERENCES timeslots(id),
    room_id INTEGER NOT NULL REFERENCES rooms(id),
    week_pattern TEXT NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_week_pattern CHECK (week_pattern IN ('BOTH', 'ODD', 'EVEN')),
    CONSTRAINT ck_status CHECK (status IN ('PLANNED', 'CONFIRMED', 'CANCELLED'))
);

CREATE INDEX ix_se_offering ON schedule_entries(offering_id);
CREATE INDEX ix_se_timeslot ON schedule_entries(timeslot_id);
CREATE INDEX ix_se_room ON schedule_entries(room_id);
CREATE INDEX ix_se_status ON schedule_entries(status);
