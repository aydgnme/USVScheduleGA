# USV Schedule - Database Architecture Documentation

## Overview
Complete university scheduling system for Universitatea "Ștefan cel Mare" din Suceava (USV), a Romanian public research university with 11 faculties, ~1,900 faculty members, 87 specialization programs, and 155 active teaching spaces.

## Data Structure

### Organizational Hierarchy
```
Faculties (11)
├── FIESC (Electrical Engineering & Computer Science)
├── FIM (Mechanical Engineering)
├── FEFS (Physical Education & Sport)
├── FIA (Food Engineering)
├── FIG (History, Geography & Social Sciences)
├── FLSC (Letters & Communication Sciences)
├── SILVIC (Forestry)
├── SEAB (Economics, Administration & Business)
├── FSED (Psychology & Education Sciences)
├── FDSA (Law & Administrative Sciences)
└── FMSB (Medicine & Biological Sciences)

Departments (24)
├── Calculatoare (Computers) - FIESC
├── Electrotehnică (Electrical) - FIESC
├── Automatică (Automation) - FIESC
├── Mecanică (Mechanics) - FIM
├── Autovehicule (Vehicles) - FIM
└── ... (19 more departments)

Specializations (87+)
├── AIA (Automatică și Informatică Aplicată) - BACHELOR
├── CSEE (Cibernetică, Statistică și IE) - MASTER
├── PIPP (Programe Psihopedagogice) - CONVERSION
└── ... (84 more specializations)
```

### Study Types (from groups.json)
- **Type 0**: Postgraduate/Modular Programs
- **Type 1**: Undergraduate Bachelor (4 years)
- **Type 2**: Postgraduate Master (2 years)
- **Type 3**: Conversion/Professional Programs

### Teaching Spaces (155 active)
- **Building C**: 28 rooms (Primary teaching facility)
- **Building A**: 27 rooms (Main lectures & admin)
- **Building D**: 14 rooms (Amphitheaters)
- **Building E**: 15 rooms (Medical/Healthcare labs)
- **Building B**: 11 rooms (Specialized labs)
- **Building J**: 4 rooms (Small seminars)
- **Virtual**: 1 online classroom (999 capacity for distance learning)

### Teachers
- ~1,900 faculty members
- Support for multiple department affiliations
- Department availability and weekly hour constraints
- Teacher profiles linked to authentication system

### Student Groups
- 8,461+ groups across all specializations
- Support for subgroups (a, b, c) for large cohorts
- Modular vs. regular programs
- Study years 1-4 for bachelor, 1-2 for master

## Database Schema

### Core Tables

#### `faculties`
- id (PK)
- code (unique, e.g., 'FIESC', 'FIM')
- name (full faculty name)
- short_name

#### `departments`
- id (PK)
- faculty_id (FK)
- code (unique per faculty)
- name

#### `specializations`
- id (PK)
- department_id (FK)
- code (unique)
- name
- study_cycle (BACHELOR/MASTER/DOCTORATE/CONVERSION)

#### `groups` (Student cohorts)
- id (PK)
- specialization_id (FK)
- code (unique, e.g., 'AIA-001', 'C-002')
- study_year (1-4)
- group_number
- subgroup_index (a, b, c, or NULL)
- is_modular (0/1)

#### `courses`
- id (PK)
- code (unique)
- title
- component_type (LECTURE/LAB/SEMINAR/PROJECT)
- credits
- semester
- parity (BOTH/ODD/EVEN)

#### `course_offerings`
- id (PK)
- course_id (FK)
- group_id (FK)
- teacher_id (FK)
- weekly_hours
- parity (BOTH/ODD/EVEN)

#### `rooms`
- id (PK)
- code (unique, e.g., 'C001', 'A115')
- capacity
- room_type (LECTURE_HALL/LAB/SEMINAR_ROOM/MEETING/VIRTUAL/OTHER)
- building (C, A, D, E, B, J, VIRTUAL)
- computers (count if applicable)
- features_json

#### `timeslots`
- id (PK)
- day_of_week (MONDAY-FRIDAY)
- start_time (HH:MM)
- end_time (HH:MM)
- duration_minutes

#### `schedule_entries`
- id (PK)
- offering_id (FK)
- timeslot_id (FK)
- room_id (FK)
- week_pattern (BOTH/ODD/EVEN)
- status (PLANNED/CONFIRMED/CANCELLED)
- created_at

#### `users` (Authentication)
- id (PK)
- email (unique)
- password (hashed)
- role (TEACHER/SECRETARY/ADMIN)
- enabled

#### `teacher_profiles`
- id (PK)
- user_id (FK, unique)
- first_name
- last_name
- max_hours_weekly
- available_days_json (CLOB)
- preferred_time
- note
- created_at

#### `teacher_departments` (Many-to-Many)
- teacher_id (FK)
- department_id (FK)
- PRIMARY KEY (teacher_id, department_id)

## Migration Scripts

### V1 - Schema Initialization
- Creates all 13 core tables
- Defines constraints (enum validation via CHECK)
- Establishes foreign key relationships
- Creates Oracle sequences for ID generation
- Builds indices for performance

### V2 - Legacy Data Migration
- Safe PL/SQL block with exception handling
- Migrates data from old schema if it exists
- Updates sequences to prevent PK conflicts

### V3 - Deprecated
- Now empty (data moved to V4-V8)
- Kept for backward compatibility

### V4 - Faculties & Departments
- Inserts 11 faculties matching USV official structure
- Creates 24 departments with proper faculty associations
- Updates sequences post-insertion

### V5 - Specializations & Groups
- Inserts 35+ specialization definitions
- Creates representative groups (50+) across all specializations
- Maps study cycles correctly (BACHELOR/MASTER/CONVERSION)

### V6 - Teachers
- Inserts 15 sample teachers with realistic data
- Links teachers to multiple departments
- Creates user accounts for authentication
- Sets availability and weekly hour constraints

### V7 - Rooms
- Inserts 40+ rooms across buildings C, A, D, E, B, J
- Classifies room types (LECTURE_HALL, LAB, SEMINAR_ROOM)
- Adds virtual classroom for distance learning
- Includes computer lab information (C207: 14 computers)

### V8 - Timeslots
- Creates 20 standard timeslots (Mon-Fri, 08:00-16:00, 2-hour blocks)
- Sets duration_minutes for each slot
- Aligns with typical university scheduling

## Key Design Decisions

### 1. Normalized Architecture
- **Problem**: Original flat structure couldn't represent multiple departments per teacher
- **Solution**: Separate `teacher_departments` junction table (many-to-many relationship)
- **Benefit**: Single source of truth for relationships; prevents data anomalies

### 2. Specialization-First Groups
- **Problem**: Groups need to belong to specializations within departments
- **Solution**: groups.specialization_id → specializations.id → departments.id → faculties.id
- **Benefit**: Clear hierarchical structure; easy filtering by department/faculty

### 3. Enum Validation
- **Problem**: Type safety for study_cycle, room_type, status fields
- **Solution**: CHECK constraints in Oracle (e.g., `CHECK (study_cycle IN ('BACHELOR', 'MASTER', 'DOCTORATE', 'CONVERSION'))`)
- **Benefit**: Database-level enforcement; prevents invalid states

### 4. Flexible Availability Storage
- **Problem**: Teachers may have complex availability patterns
- **Solution**: available_days_json (CLOB) for flexibility; structured data when needed
- **Benefit**: Extensible without schema changes; can store complex constraints

### 5. Study Year Support
- **Problem**: Groups span 1-4 years (or 1-2 for master)
- **Solution**: study_year field (1-4) with CHECK constraint
- **Benefit**: Clear year progression; easy cohort identification

### 6. Parity Support
- **Problem**: Some courses taught only in odd/even weeks (semester halves)
- **Solution**: parity field in courses and course_offerings (BOTH/ODD/EVEN)
- **Benefit**: Handles Romanian university calendar quirks

### 7. Schedule Status Tracking
- **Problem**: Need to distinguish planned vs. confirmed vs. cancelled entries
- **Solution**: status field in schedule_entries (PLANNED/CONFIRMED/CANCELLED)
- **Benefit**: Workflow support; audit trail capability

## Data Statistics

| Entity | Count | Notes |
|--------|-------|-------|
| Faculties | 11 | Official USV structure |
| Departments | 24 | Across all faculties |
| Specializations | 87+ | Including conversions |
| Student Groups | 8,461+ | From groups.json |
| Teachers | ~1,900 | From teacher.json |
| Rooms (active) | 155 | Valid teaching spaces |
| Rooms (all) | 731 | Including labs/offices |
| Timeslots | 20 | Standard 2-hour blocks |

## Future Enhancements

### Possible Additions
1. **Course Prerequisites**: Direct acyclic graph for course dependencies
2. **Room Equipment**: Detailed inventory (projectors, boards, etc.)
3. **Conflict Detection**: Automated scheduling conflict resolution
4. **Capacity Planning**: Load balancing across teaching spaces
5. **Teacher Preferences**: Weighted constraint satisfaction for assignments
6. **Virtual Classrooms**: Support for hybrid learning (Zoom, Teams integration)
7. **Attendance Tracking**: Integration with student management system
8. **Audit Logs**: Change history for compliance

## Performance Considerations

### Indices Created
- `uk_users_email` on users(email) - Fast authentication lookup
- `uk_courses_code` on courses(code) - Fast course discovery
- `uk_rooms_code` on rooms(code) - Fast room lookup
- `uk_groups_code` on groups(code) - Fast group discovery
- `ix_*_user`, `ix_*_faculty`, `ix_*_dept` - Foreign key joins
- `ix_rooms_building` - Filter rooms by building
- `ix_se_status` - Fast schedule status queries

### Query Optimization Tips
1. Always join through specialization → department → faculty for hierarchical queries
2. Use `day_of_week` index for timeslot lookups
3. Cache department availability data (frequently accessed)
4. Consider materializing views for complex teacher workload queries

## Migration Path from Legacy

1. **V1**: Create new schema (existing tables unchanged initially)
2. **V2**: Migrate legacy data with exception handling
3. **V3-V8**: Seed reference data (faculties, departments, etc.)
4. **Parallel Run**: Run new system alongside legacy
5. **Cutover**: When new system verified, deprecate old schema
6. **Cleanup**: Archive old tables after confirmation period

## Security Notes

- All passwords stored hashed (bcrypt/Argon2 recommended)
- Role-based access control (TEACHER/SECRETARY/ADMIN)
- Teacher profiles linked 1:1 to users for separation of concerns
- Audit timestamps (created_at) on sensitive entities
- Consider row-level security (RLS) for multi-tenant future

---

*Last Updated: December 15, 2025*
*Database: Oracle 11g+*
*Framework: Spring Boot 3.5.8 + JPA/Hibernate*
*Migration Tool: Flyway 9.22.3*
